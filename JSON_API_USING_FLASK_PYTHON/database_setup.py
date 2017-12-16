import sys

from sqlalchemy import Column, ForeignKey, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import create_engine

Base = declarative_base() # This is the declarative base class which lets sqlalchemy know that our classes are special sqlalchemy classes that corresponds to tables in our database

class Restaurant(Base):

	__tablename__ = 'restaurant'

	name = Column(String(80), nullable = False)
	id = Column(Integer, primary_key = True)

class MenuItem(Base):

	__tablename__ = 'menu_item'

	name = Column(String(80), nullable = False)
	id = Column(Integer, primary_key = True)
	course = Column(String(250))
	description = Column(String(250))
	price = Column(String(8))
	restaurant_id = Column(Integer, ForeignKey('restaurant.id'))
	restaurant = relationship(Restaurant)
	
	# Following part of the class is used to respond MenuItem data via JSON format
	@property
	def serialize(self): # (for responding via JSON)This function will help define what data we want to send across
		#Returns object data in easily serializable format
		return{
			'name' : self.name,
			'description' : self.description,
			'id' : self.id,
			'price' : self.price,
			'course' : self.course
		}
# We are using sqlite3
engine = create_engine('sqlite:///restaurantmenu.db')

#This will add the classes that will be created as new tables in the database
Base.metadata.create_all(engine)
