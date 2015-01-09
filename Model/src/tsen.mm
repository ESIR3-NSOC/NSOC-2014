
class tsen.Room  {
    @id
    name : String

    @contained
    humiditySensor : tsen.Humidity
    @contained
    temperatureSensor : tsen.IndoorTemperature
    @contained
    airQualitySensor : tsen.CO2
    @contained
    valves : tsen.Heater
    @contained
    lesson : tsen.Activity[0,*]
}

class tsen.Humidity  {
    name : String
    value : Double
}

class tsen.CO2 {
    name : String
    value :Double
}

class tsen.IndoorTemperature  {
    name : String
    value : Double
}

class tsen.Heater  {
    name : String
    value : Double
}

class tsen.Activity  {
    name : String
    start : Long
    end : Long
    @contained
    members : tsen.User[0,*]
    @contained
    targetedTemperature : tsen.Temperature
}

class tsen.User  {
    name : String
    wishState : String
}

class tsen.Temperature  {
    value : Double
}
