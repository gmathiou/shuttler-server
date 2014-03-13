# Shuttler REST api
API for Shuttler, a crowdsourcing iOS app.

Github for the [client side](https://github.com/gmathiou/shuttler) and the [server side](https://github.com/gmathiou/shuttler-server)

# Group Public calls

# Stops [/stops]
Returns all registered stops.

## Retrieve Message [GET]

+ Response 200 (application/json)

    + Body

            {
                "stops":[
                    {
                        "id":1,
                        "name":"Place de l'Etoile",
                        "shortname":"etoile",
                        "longitude":2.2949,
                        "latitude":48.873934,
                        "lineId":1
                    },
                    {
                        "id":2,
                        "name":"Porte Dauphine",
                        "shortname":"dauphine",
                        "longitude":2.274401,
                        "latitude":48.87028,
                        "lineId":1
                    }
                ]
            }

# Bus lines [/lines]
Returns all registered bus lines.

## Retrieve Message [GET]

+ Response 200 (application/json)

    + Body

            {
                "lines":[
                    {
                        "id":1,
                        "name":"Paris - INRIA",
                        "stops_sequence":[ 1, 2, 3, 7 ]
                    },
                    {
                        "id":2,
                        "name":"Versailles - INRIA",
                        "stops_sequence":[ 4, 5, 6, 7 ]
                    }
                ]
            }

# User hop-on [/hopon]
Called by user to indicate the boarding on the bus.

## Retrieve Message [POST]
+ Request POST (application/json)

    + Body

            {
                "email":"mathioudakis.giorgos@gmail.com",
                "lat":"48.885944",
                "lon":"2.2949",
                "line":"1"
            }

+ Response 200

# Update location [/updatelocation]
Updates the location of the user on the server.

## Retrieve Message [POST]
+ Request POST (application/json)

    + Body

            {
                "email":"mathioudakis.giorgos@gmail.com",
                "lat":"48.885944",
                "lon":"2.2949",
                "lastSeenStopID":"2"
            }

+ Response 200

# User hop-off [/hopoff]
Called by user to indicate the disembarkment from the bus.

## Retrieve Message [POST]
+ Request POST (application/json)
    + Body

            {
                "email":"mathioudakis.giorgos@gmail.com",
                "kilometers":16.89
            }

+ Response 200

# Buses for line [/busesforline/{email}/{line}]
Returns buses of the line specified.

## Retrieve Message [GET]

+ Response 200 (application/json)

    + Body

            {
                "buses":[
                    {
                        "lineid":1,
                        "longitude":2.236996,
                        "latitude":48.899377,
                        "lastSeenStopID":1
                    }
                ]
            }

# User Profile [/profile/{email}]
Retrieves user stats.

## Retrieve Message [GET]

+ Response 200 (application/json)

    + Body

            {
                "rank":1,
                "email":"mathioudakis.giorgos@gmail.com",
                "views":23,
                "kilometers":18
            }