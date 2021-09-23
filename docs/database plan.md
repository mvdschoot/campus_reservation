User(username, password, type)
-  Username: String
-  Password: String (User password is not stored as plain text)
-  Type: int\
o  0: admin\
o  1: teacher\
o  2: student

Reservations(id, username, room, date, starting_time, ending_time)
-  Id: integer
-  Username: String (Same as User's username)
-  Room: int (Room id)
-  Date: Date (String ('yyyy-mm-dd'))
-  Starting_time: String 'hh:mm:ss'
-  Ending_time: String 'hh:mm:ss'

Room(id, name, building, teacher_only, capacity, photos, description)
-  Id: int
-  Name: String
-  Building: int (Building id)
-  Teacher_only: Boolean
-  Capacity: int
-  Photos: String (url to photos)
-  Description: String
-  Type: String (Project room, lecture hall)

Building(id, name, roomCount, address, available_bikes, max_bikes)
-  Id: int
-  Name: String
-  Room_count: int
-  Address: String
-  Available_bikes: int
-  Max_bikes: int

Bike_reservation(id, building, numBikes, date, starting_time, ending_time)
-  Id: int
-  User: int
-  Building: int
-  Num_bikes: int
-  Date: Date (String ('yyyy-mm-dd'))
-  Starting_time: String 'hh:mm:ss'
-  Ending_time: String 'hh:mm:ss'

CalenderItems(id, user, title, date, starting_time, ending_time, description)
-  Id: int
-  User: String
-  Title: String
-  Date: Date (String ('yyyy-mm-dd'))
-  Starting_time: String 'hh:mm:ss'
-  Ending_time: String 'hh:mm:ss'
-  Description: String

Food(id, name, price)
-  Id: int
-  Name: String
-  Price: float

Food_building(building, food)
-  Building: int (foreign key building id)
-  Food: int (foreign key food id)

Food_reservations(reservation, food, quantity)
-  Reservation: int (foreign key reservation id)
-  Food: int (foreign key food id)
-  Quantity: int