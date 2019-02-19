from flask import Flask
from flask_restful import Api, Resource, reqparse

import time
import random
import logging

logging.basicConfig(level='CRITICAL')

app = Flask(__name__)
api = Api(app)

ipas = [
    {
        "name": "Roleta Russa",
        "price": 28
    },
    {
        "name": "Rota do Cerrado",
        "price": 10
    }
]

class Ipa(Resource):
    def get(self):
        return ipas, 200

api.add_resource(Ipa, "/ipas")


class Lager(Resource):
    def get(self):
        logging.critical('Lager ERROR')
        raise Exception('Error getting lager beer!')
      
api.add_resource(Lager, "/lagers")


pilsens = [
    {
        "name": "Skol",
        "price": 1
    }
]

class Pilsen(Resource):
    def get(self):
        time.sleep(10)
        return pilsens, 200
      
api.add_resource(Pilsen, "/pilsens")


stouts = [
    {
        "name": "Guinness",
        "price": 9
    }
]

class Stout(Resource):
    def get(self):
        n = random.randint(1,3)
        if (n == 1):
            time.sleep(10)
            logging.critical('Stout WAITING following by an error')
            raise Exception('Error after timeout getting stout beer!')
        elif n == 2:
            logging.critical('Stout ERROR')
            raise Exception('Error getting stout beer!')
        else:
            logging.critical('Stout SUCCESS')
            return stouts, 200


api.add_resource(Stout, "/stouts")


app.run(debug=False, host='0.0.0.0')