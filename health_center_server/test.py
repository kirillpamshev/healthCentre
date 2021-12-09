import json
import uuid
from datetime import datetime, date

from flask import Flask, request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import exc, ForeignKey
from sqlalchemy.orm import relationship

h_start = 8
h_end = 20
interval_min = 30

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///sqlite3_bd_hc.db'
db = SQLAlchemy(app)
db.Model.metadata.reflect(db.engine)


class Client(db.Model):
    __tablename__ = 'Clients'
    __table_args__ = {'extend_existing': True}
    login = db.Column(db.Text, primary_key=True, unique=True, nullable=False)
    lastName = db.Column(db.Text, nullable=False)
    firstname = db.Column(db.Text, nullable=False)
    midName = db.Column(db.Text, nullable=True)
    phone = db.Column(db.Integer, unique=True, nullable=False)
    password = db.Column(db.Text, nullable=False)


@app.route('/api/get_history', methods=['POST'])
def getHistory():
    request_data = request.get_json()
    records = Record.query.filter_by(login=request_data).all()
    if len(records) == 0:
        return json.dumps([])
    else:
        history = [{
                       "name": f"{record.doctor.lastName:s} {record.doctor.firstName:s} {record.doctor.midName:s}",
                       "spec": record.doctor.spec.nameSpec,
                       "status": record.codeStatus,
                       "datetime": f"{record.date:s} {record.time}"} for record in records]
        return json.dumps(history)


@app.route('/api/login', methods=['POST'])
def getLogin():
    request_data = request.get_json()
    login = request_data["login"]
    passw = request_data["passwordHash"]
    client = Client.query.filter_by(login=login).first()
    if client is not None:
        if client.password == passw:
            return json.dumps({"isGranted": True})
        else:
            return json.dumps({"isGranted": False})
    else:
        return json.dumps({"isGranted": False})


@app.route('/api/register', methods=['POST'])
def addUser():
    request_data = request.get_json()
    login = request_data["login"]
    passw = request_data["passwordHash"]
    firstname = request_data["firstname"]
    lastname = request_data["lastname"]
    phone = request_data["phone"]
    client = Client(login=login, lastName=lastname, firstname=firstname, midName=None, phone=phone,
                    password=passw)
    try:
        db.session.add(client)
        db.session.commit()
        return json.dumps({"isOk": True, "error": ""})
    except exc.IntegrityError:
        db.session.rollback()
        return json.dumps({"isOk": False, "error": "Login or phone number is not UNIQUE"})


class Spec(db.Model):
    __tablename__ = 'Specs'
    __table_args__ = {'extend_existing': True}
    codeSpec = db.Column(db.Integer, primary_key=True, nullable=False)
    nameSpec = db.Column(db.Text, nullable=False)


@app.route('/api/get_specs', methods=['GET'])
def getSpecs():
    specs = Spec.query.all()
    return json.dumps([{"spec_name": i.nameSpec} for i in specs])


class Doctor(db.Model):
    __tablename__ = 'Doctors'
    __table_args__ = {'extend_existing': True}
    codeDoctor = db.Column(db.Integer, primary_key=True, nullable=False)
    lastName = db.Column(db.Text, nullable=False)
    firstName = db.Column(db.Text, nullable=False)
    midName = db.Column(db.Text, nullable=True)
    codeSpec = db.Column(db.Integer, ForeignKey("Specs.codeSpec"), nullable=False)
    spec = relationship("Spec")


@app.route('/api/get_doctors', methods=['POST'])
def getDoctors():
    spec = request.get_json()
    doctors = Doctor.query.filter_by(
        codeSpec=Spec.query.filter_by(nameSpec=spec).first().codeSpec).all()
    return json.dumps(
        [{"id_doctor": i.codeDoctor, "LFM_names": f"{i.lastName:s} {i.firstName:s} {i.midName:s}"}
         for i in doctors])


@app.route('/api/get_info', methods=['GET'])
def getInfo():
    return json.dumps({"nameHC": "Центр здоровья", "addressHC": "г.Москва ул.Савёловская д.5",
                       "contactMain": "+7(888)9000000", "contactHot": "+7(999)9000000"})


class Record(db.Model):
    __tablename__ = 'Services'
    __table_args__ = {'extend_existing': True}
    idService = db.Column(db.Integer, primary_key=True, unique=True, nullable=False)
    codeDoctor = db.Column(db.Integer, ForeignKey("Doctors.codeDoctor"), nullable=False)
    login = db.Column(db.Text, ForeignKey("Clients.login"), nullable=False)
    time = db.Column(db.Text, nullable=False)
    date = db.Column(db.Text, nullable=False)
    codeStatus = db.Column(db.Integer, nullable=False)
    client = relationship("Client")
    doctor = relationship("Doctor")


@app.route('/api/get_time', methods=['POST'])
def getTime():
    request_data = request.get_json()
    id_doctor = request_data["id_doctor"]
    date_str = request_data["date"]
    req_date = datetime.strptime(date_str, '%B %d, %Y').date()
    date_now = date.today()
    time_now = datetime.now().time()
    if req_date < date_now:
        return json.dumps([])
    times = [{"time": f"{i // 60:d}:{i % 60:0>2d}"} for i in
             range(h_start * 60, h_end * 60, interval_min)]
    if req_date == date_now:
        times = list(
            filter(lambda x: datetime.strptime(x["time"], '%H:%M').time() > time_now, times))
    records = Record.query.filter_by(date=date_str, codeDoctor=id_doctor).all()
    if len(records) != 0:
        busy_times = [record.time for record in records]
        times = [time for time in times if time["time"] not in set(busy_times)]
    return json.dumps(times)


@app.route('/api/get_service', methods=['POST'])
def getService():
    request_data = request.get_json()
    codeDoctor = request_data["codeDoctor"]
    login = request_data["login_bd"]
    date = request_data["date"]
    time = request_data["time"]
    record = Record(idService=(uuid.uuid1().int >> 65), login=login, codeDoctor=codeDoctor,
                    date=date, time=time, codeStatus=0)
    try:
        db.session.add(record)
        db.session.commit()
        return json.dumps({"isOk": True, "error": ""})
    except exc.IntegrityError as er:
        db.session.rollback()
        return json.dumps({"isOk": False, "error": str(er)})