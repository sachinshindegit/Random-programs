from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import cgi
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from database_setup import Restaurant, Base, MenuItem

engine = create_engine('sqlite:///restaurantmenu.db')
Base.metadata.bind = engine
DBSession = sessionmaker(bind=engine)
session = DBSession()

class myWebserverHandler(BaseHTTPRequestHandler):
	def do_GET(self):
		try:
			if self.path.endswith("/restaurants"):
				# This will list all the restaurants
				self.send_response(200)
				self.send_header('Content-type', 'text/html')
				self.end_headers()

				restaurants = session.query(Restaurant).all()
				output = "<a href='/restaurants/new'>Create a new Restaurant</a></br>"
				output += "<html><body>"
				output += ""
				for rest in restaurants:
					output += rest.name
					output += "</br>"
					output += "<a href='/restaurants/%s/edit'>Edit</a>" % rest.id
					output += "</br>"
				output += "</body></html>"
				self.wfile.write(output)
				return
			
			if self.path.endswith("/restaurants/new"):
				self.send_response(200)
				self.send_header('Content-type', 'text/html')
				self.end_headers()
				output = ""
				output += "<html><body>"
				output += "<h1>Make a New Restaurant</h1>"
				output += "<form method = 'POST' enctype='multipart/form-data' action = '/restaurants/new'>"
				output += "<input name = 'newRestaurantName' type = 'text' placeholder = 'New Restaurant Name' > "
				output += "<input type='submit' value='Create'>"
				output += "</form></body></html>"
				self.wfile.write(output)
				return

			if self.path.endswith("/edit"):
				restaurantIDPath = self.path.split("/")[2] #Since index 2 has the restaurant id in the url
				myRestaurantQuery = session.query(Restaurant).filter_by(id=restaurantIDPath).one()
				if myRestaurantQuery:
					self.send_response(200)
					self.send_header('Content-type', 'text/html')
					self.end_headers()
					output = "<html><body>"
					output += "<h1>"
					output += myRestaurantQuery.name
					output += "</h1>"
					output += "<form method='POST' enctype='multipart/form-data' action = '/restaurants/%s/edit' >" % restaurantIDPath
					output += "<input name = 'newRestaurantName' type='text' placeholder = '%s' >" % myRestaurantQuery.name
					output += "<input type = 'submit' value = 'Rename'>"
					output += "</form>"
					output += "</body></html>"

  					self.wfile.write(output)

			if self.path.endswith("/hello"):
				self.send_response(200)
				self.send_header('Content-type', 'text/html')
				self.end_headers()

				output = ""
				output += "<html><body>Hello!!!!!"
				output += "<form method='POST' enctype='multipart/form-data' action=hello'><h2>What is your message?<h2><input name='mymessage' type='text' ><input type='submit' value='submit'></form>"
				output += "</body></html>"
				self.wfile.write(output)
				print output
				return

			if self.path.endswith("/voila"):
				self.send_response(200)
				self.send_header('Content-type', 'text/html')
				self.end_headers()

				output = ""
				output += "<html><body>voila!!!!!</body></html>"
				self.wfile.write(output)
				print output
				return

		except IOError:
			self.send_error(404, "File Not Found %s" %self.path)

	def do_POST(self):
		try:
			if self.path.endswith("/edit"):
				ctype, pdict = cgi.parse_header(self.headers.getheader('content-type'))
				if ctype == 'multipart/form-data':
					fields=cgi.parse_multipart(self.rfile, pdict)
					messagecontent = fields.get('newRestaurantName')
					restaurantIDPath = self.path.split("/")[2] #Since index 2 has the restaurant id in the url
					myRestaurantQuery = session.query(Restaurant).filter_by(id=restaurantIDPath).one()
					if myRestaurantQuery != []:
						myRestaurantQuery.name = messagecontent[0]
						session.add(myRestaurantQuery)
						session.commit()
						self.send_response(301)
						self.send_header('Content-type', 'text/html')
						self.send_header('Location', '/restaurants')
						self.end_headers()

			if self.path.endswith("/restaurants/new"):
				ctype, pdict = cgi.parse_header(self.headers.getheader('content-type'))
				if ctype == 'multipart/form-data':
					fields=cgi.parse_multipart(self.rfile, pdict)
					messagecontent = fields.get('newRestaurantName')
					# Create new Restaurant Object
					newRestaurant = Restaurant(name=messagecontent[0])
					session.add(newRestaurant)
					session.commit()
					self.send_response(301)
					self.send_header('Content-type', 'text/html')
					self.send_header('Location', '/restaurants')
					self.end_headers()


			"""self.send_response(301)
			self.end_headers()

			ctype, pdict = cgi.parse_header(self.headers.getheader('content-type'))
			if ctype == 'multipart/form-data':
				fields=cgi.parse_multipart(self.rfile, pdict)
				messagecontent = fields.get('mymessage')

			output = ""
			output += "<html><body>"
			output += "<h2> Okay, How about this: <h2>"
			output += "<h1>%s<h1>"%messagecontent[0]
			output += "</body></html>"
			self.wfile.write(output)
			print output"""

		except:
			pass

def main():
	try:
		port = 8080
		server = HTTPServer(('',port), myWebserverHandler)
		print "Web server running on port %s" %port
		server.serve_forever()

	except KeyboardInterrupt:
		print "^c entered, stopping the web server..."
		server.socket.close()

if __name__=='__main__':
	main()
