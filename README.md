# Sustain_CedricSebEvanJean

Android app to help students to identify their CO2 footprint and its consequences.

Known issues:

-> Adding remote trip with bicycle/pedestrian/public transport does not work because of a 
problem in the api server.

-> Dates are in UTC time, they have not been converted to local time zone

-> Weather api does not save to bundle even though it should

-> Carpool returns 0 in calculator (problem with server)

-> A few images only support xxhdpi instead of both xxhdpi and mhdpi

-> Some strings like car_pooling or public_transport have not been parsed into better strings 

-> Total CO2 is not implemented:(
