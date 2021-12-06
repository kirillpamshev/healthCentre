from flask import Flask, request

app = Flask(__name__)


@app.route('/api/get_history', methods=['POST'])
def getHistory():
	request_data = request.get_json()
	print(request_data)
	return '[{"name":"Федоренко Иван Алексеевич", "spec":"Хирург", "datetime":"2021.1.12", "status":0}, {"name":"Кузьмин Антон Павлович", "spec":"Врач-Педиатр", "datetime":"2021.1.12", "status":1}]'

@app.route('/api/login', methods=['POST'])
def getLogin():
	request_data = request.get_json()
	login = request_data["login"]
	passw = request_data["passwordHash"]
	print(login, passw)
	return '{"isGranted":true}'
	
@app.route('/api/register', methods=['POST'])
def addUser():
	request_data = request.get_json()
	login = request_data["login"]
	passw = request_data["passwordHash"]
	firstname = request_data["firstname"]
	lastname = request_data["lastname"]
	phone = request_data["phone"]
	print(login, passw, firstname, lastname, phone)
	return '{"isOk":false, "error":"Login is not"}'
	
@app.route('/api/get_specs', methods=['GET'])
def getSpecs():
    return '[{"spec_name":"Стоматолог"},{"spec_name":"Терапевт"},{"spec_name":"Окулист"}]'
	
@app.route('/api/get_doctors', methods=['POST'])
def getDoctors():
    return '[{"id_doctor":1,"LFM_names":"Васнецов Генадий Андреевич"},{"id_doctor":2,"LFM_names":"Локин Артём Алексеевич"},{"id_doctor":3,"LFM_names":"Батков Михаил Евгеньевич"}]'
	
@app.route('/api/get_info', methods=['GET'])
def getInfo():
    return '{"nameHC":"Центр здоровья","addressHC":"г.Москва ул.Савёловская д.5","contactMain":"+7(888)9000000","contactHot":"+7(999)9000000"}'
    
@app.route('/api/get_time', methods=['POST'])
def getTime():
    request_data = request.get_json()
    id_doctor = request_data["id_doctor"]
    date = request_data["date"]
    print(id_doctor, date)
    return '[{"time":"18:30"},{"time":"19:30"}]'
    
@app.route('/api/get_service', methods=['POST'])
def getService():
    request_data = request.get_json()
    codeDoctor = request_data["codeDoctor"]
    login_bd = request_data["login_bd"]
    date = request_data["date"]
    time = request_data["time"]
    print(codeDoctor, login_bd, date, time)
    return '{"isOk":true, "error":""}'
