from flask import Flask, request

app = Flask(__name__)


@app.route('/api/get_history', methods=['POST'])
def getHistory():
	request_data = request.get_json()
	print(request_data)
	return '[{"name":"Иван", "spec":"Хиррург", "datetime":"2021.1.12", "status":0}, {"name":"Мария", "spec":"Хиррург", "datetime":"2021.1.12", "status":1}]'

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
    return '[{"name_spec":"Стоматолог"},{"name_spec":"Терапевт"},{"name_spec":"Окулист"}]'