from flask import Flask
from flask import request, jsonify

app = Flask(__name__)
app.config.from_pyfile('config.py')

@app.route('v1/message', methods=['POST'])
def handle_message():
    message = request.json.get('message')