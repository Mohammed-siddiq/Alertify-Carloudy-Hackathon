# Alertify-Carloudy-Hackathon
Gives crime alerts for the rider when he/she is en-route to a crime prone area. Also, on the same lines, suggests the user with nearby events and offers around them.

# Inspiration 

Carloudy offers a unique solution to the problem we face day to day. The on-screen guide for the car seems to be an amazing inspiration for whole lot of new ideas. It is joyous to drive along the roads of cities and towns but for every good thing there will always be a negative side. Active crime is one of the biggest threats, there are places to which we tread/travel oblivious to the danger in the place and its crime history. Whenever user is heading towards unknown place or is in a neighborhood about which he doesn’t have any idea it is always better to know what you are heading into before you start. Users sometimes miss the most happening things around (like offers/events) despite being in its vicinity.

# What it does 

It gives crime alerts for the rider when he/she is en-route to a crime prone area. Also, on the same lines, it suggests the user with nearby events and offers around them based on their preferences.

# How we built it 

The system tracks user’s location in real time to constantly monitor for crime prone areas and notifies the user if it is a high alert zone. Extending the idea of location-based recommendation, we made use of various other APIs to recommend the most happening events and impressive deals or offers, on the fly. The user has the control over the alerts/notification. If the user is heading to a known place or is on a routine journey, he/she can opt out of the alerts. On the flipside, if the user is unsure of the neighborhood or the place where he/she is heading towards: Enable notifications, and feel safe!

# The App 

* The user has the option to choose what alerts he wishes to see on the go: Crime alerts, Event alerts and nearby happening deals. 

* We track the user’s location in real time at regular intervals, we have written Location Listener to accomplish the same.  

* A broadcast receiver is used to constantly get the updated location. 

* Upon getting the location, we query the APIs to get the data and filter out suitable information from that.  

* City of Chicago keeps a comprehensive record of the data and updates the data real-time, we have used the API’s provided by the government of Chicago : https://dev.socrata.com/foundry/data.cityofchicago.org/6zsd-86xi 

* Querying the above APi with location details at specific intervals of time and inferring whether that location and it’s surroundings is safe. If it is not, then notify the user with the same. 

* On the same lines, if user opts in to get notified with the events/offers, If he/she is in the vicinity of offers/events then notify the user with the same. 

* The app stores the user preferences and user can change the preferences before any journey when he/she feels safe (or vice versa) 



# What's next for this app 

* User can reroute to a different path after he/she gets notified. 

* The notification of events/offers can be personalized.   

* Specifically, the app can make use of machine learning and analyze the user’s interests and suggest events based on the user’s preferences.  

 

