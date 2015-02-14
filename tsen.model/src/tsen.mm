
class tsen.Room  {
    @id
    name : String
    @contained
    Measurement : tsen.Sensor[0,*]

    @contained
    member : tsen.User[0,*]
}

class tsen.Sensor {

    @id
    groupAddress : String

    sensorId : String
    sensorType : String
    associatedDPT : String
    value : String
    scale : String
}



class tsen.User  {
    @id
    id : String
    lesson : String
    targetTemp : Double

    vote : String

}


