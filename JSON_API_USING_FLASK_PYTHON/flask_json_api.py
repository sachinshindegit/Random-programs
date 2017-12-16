from flask import Flask, request, jsonify
app = Flask(__name__) # creating instance of the class with __name__ which is a variable which automatically gets defined for  class in python

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from database_setup import Base, Restaurant, MenuItem

engine = create_engine('sqlite:///restaurantmenu.db')
Base.metadata.bind = engine

DBsession = sessionmaker(bind=engine)
session = DBsession()

#Making an API Endpoint (GET Request)
@app.route('/restaurants/<int:restaurant_id>/menu/JSON') #http://localhost:5000/restaurants/4/menu/JSON
def restaurantMenuJSON(restaurant_id):
	restaurant = session.query(Restaurant).filter_by(id=restaurant_id).one()
	items = session.query(MenuItem).filter_by(restaurant_id=restaurant_id).all()
	return jsonify(MenuItems=[i.serialize for i in items])

@app.route('/restaurants/<int:restaurant_id>/menu/<int:menu_id>/JSON') #http://localhost:5000/restaurants/4/menu/11/JSON
def menuItemJSON(restaurant_id,menu_id):
	menuItem = session.query(MenuItem).filter_by(id=menu_id).one()
	return jsonify(menuItem.serialize)

if __name__ == '__main__':   # this if makes sure that the script will run only if it is run from a python interpreter and not imported as a module
	app.secret_key = "super_secret_key"  # This will be used by flask to create sessions. In real world, this should be a strong secret key.
	app.debug = True		# Using this, flask takes care of restarting the server everytime we modify our code. Thus we dont have to do it manually
	app.run(host = '0.0.0.0', port = 5000)  # 0.0.0.0 is used since we are using vagrant and thus we need to make it publicly available. Now it will listen on all public ip addresses