
class tsen.Room  {
    name : String
}

class tsen.Activity  {
    @id
    name : type.String
    date : type.Date
    @contained
    lesson : tsen.Room[0,*]
}

class tsen.indoorTemperature  {
    value : Double
    @id
    name : type.String
    @contained
    tempSensors : tsen.Room[0,*]
}

class tsen.Humidity  {
    value : Double
    @id
    name : type.String
    @contained
    sensors : tsen.Room[0,*]
}

class tsen.Users  {
    wishState : String
    name : type.String
    group : type.String
    @contained
    members : tsen.Activity[0,*]
}

class tsen.TargetedTemperature  {
    value : type.Double
    @contained
    jesaispas : tsen.Activity
}

class tsen.heaters  {
    value : Int
    @id
    name : type.String
    @contained
    valves : tsen.Room[0,*]
}
