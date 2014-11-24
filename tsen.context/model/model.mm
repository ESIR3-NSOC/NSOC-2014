
class tsen.room  {
    name : String
}

class tsen.room.activity  {
    @id
    name : type.String
    date : type.Date
    @contained
    lesson : tsen.Room[0,*]
}

class tsen.room.indoorTemperature  {
    value : Double
    @id
    name : type.String
    @contained
    tempSensors : tsen.Room[0,*]
}

class tsen.room.humidity  {
    value : Double
    @id
    name : type.String
    @contained
    sensors : tsen.Room[0,*]
}

class tsen.room.Activity.Users  {
    wishState : String
    name : type.String
    group : type.String
    @contained
    members : tsen.Activity[0,*]
}

class tsen.room.activity.TargetedTemperature  {
    value : type.Double
    @contained
    jesaispas : tsen.Activity
}

class tsen.heaters  {
    value : Int
    @id
    name : type.String
    @contained
    valves : tsen.room[0,*]
}
