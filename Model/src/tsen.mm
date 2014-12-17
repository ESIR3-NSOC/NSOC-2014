
class nsoc_tsen.Room  {
    name : String
    @contained
    humiditySensor : nsoc_tsen.Humidity
    @contained
    temperatureSensor : nsoc_tsen.IndoorTemperature
    @contained
    Valves : nsoc_tsen.Heater
    @contained
    lesson : nsoc_tsen.Activity[0,*]
}

class nsoc_tsen.Humidity  {
    name : String
    value : type.Double
}

class nsoc_tsen.IndoorTemperature  {
    name : String
    value : type.Double
}

class nsoc_tsen.Heater  {
    name : String
    value : type.Double
}

class nsoc_tsen.Activity  {
    name : String
    start : EDate
    end : EDate
    @contained
    members : nsoc_tsen.user[0,*]
    @contained
    targetedTemperature : nsoc_tsen.temperature
}

class nsoc_tsen.user  {
    name : String
    wishState : String
}

class nsoc_tsen.temperature  {
    value : type.Double
}
