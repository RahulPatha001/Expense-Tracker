from flask import Flask
from flask import request, jsonify
from service.messageService import MessageService
from kafka import KafkaProducer
import json

app = Flask(__name__)
app.config.from_pyfile('config.py')

messageService = MessageService()
producer = KafkaProducer(bootstrap_servers=['localhost:9092'], value_serializer= lambda v: json.dumps(v).encode('utf-8'))

@app.route('/v1/message/', methods=['POST'])
def handle_message():
    message = request.json.get('message')
    result =  messageService.process_message(message)
    return jsonify({
    "amount": result.amount,
    "merchant": result.merchant,
    "currency": result.currency
})

@app.route('/', methods=['GET'])
def handle_get():
    print("hello world")
    return "hello"



if __name__ == "__main__":
    app.run(host="localhost",port=8000,debug=True)