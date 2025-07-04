import os
from flask import Flask
from flask import request, jsonify
from .service.messageService import MessageService
from kafka import KafkaProducer
import json

app = Flask(__name__)
app.config.from_pyfile('config.py')

messageService = MessageService()
kafka_host = os.getenv('KAFKA_HOST','localhost')
kafka_port = os.getenv('KAFKA_PORT','9092')
kafka_bootstrap_servers = f"{kafka_host}:{kafka_port}"
print("kafka server :"+kafka_bootstrap_servers)
print("\n")
producer = KafkaProducer(bootstrap_servers=kafka_bootstrap_servers, value_serializer= lambda v: json.dumps(v).encode('utf-8'))

@app.route('/v1/message/', methods=['POST'])
def handle_message():
    message = request.json.get('message')
    result =  messageService.process_message(message)
    serialized_result = result.json()

    # Send the serialized result to the Kafka topic
    producer.send('expense_service', result.dict())
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
    app.run(host="localhost",port=8010,debug=True)