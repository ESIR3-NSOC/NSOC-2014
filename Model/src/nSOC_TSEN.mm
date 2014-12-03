
class nsoc_tsen.Room  {
    name : String
    @contained
    humiditySensor : nsoc_tsen.Humidity[1,*]
    @contained
    temperatureSensor : nsoc_tsen.IndoorTemperature[1,*]
    @contained
    Valves : nsoc_tsen.Heater[1,*]
    @contained
    lesson : nsoc_tsen.Activity[0,*]
}

class nsoc_tsen.Humidity  {
    name : String
    value : Double
}

class nsoc_tsen.IndoorTemperature  {
    name : String
    value : Double
}

class nsoc_tsen.Heater  {
    name : String
    value : Double
}

class nsoc_tsen.Activity  {
    name : String
    start : String
    end : String
    @contained
    members : nsoc_tsen.user[0,*]
    @contained
    targetedTemperature : nsoc_tsen.temperature[1,1]
}

class nsoc_tsen.user  {
    name : String
    wishState : String
}

class nsoc_tsen.temperature  {
    value : Double
}
