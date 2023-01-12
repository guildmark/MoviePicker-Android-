#MoviePicker: An application to find specific movies to watch

The app uses an API to get random movie suggestions, while also having the possibility of importing a 
CSV file into a database with CRUD functionality to find.

##Criterias for MoviePicker:

1. Uses a database with CRUD functionality
2. Uses some kind of API call to get data.
3. Uses a MVC design pattern.
4. Has structured code with comments.
5. Focus on accessibility
6. Implements a simple login system with user authentication and password hashing.

##Features:

. User can import CSV file from IMDB's watchlist or ratings list. (Any CSV file can be imported)
. User can filter what movies to search for by genre, year, and country.
. There should be a favorite-mode, where the user can save their favorites and only choose movies from that pile.
. The app uses a SQLite database to store entries, but can also get the data from the OMDB RESTful API.
. There is possibility to use a gesture to find movie instead of pressing the FIND-button.
. Ability for user to save their choices in a profile view. 
