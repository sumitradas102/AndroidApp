//package com.example.communitystay

//class Message {
//    var message: String? = null
//    var senderID: String? = null
//    constructor(){}
//    constructor(message: String? ,enderID: String? ){
//        this.message = message
//        this.senderID = senderID
//    }
//}
package com.example.communitystay

/*class Message {
    var message: String? = null
    var senderID: String? = null
    var receiverID: String? = null

    constructor() {}

    constructor(message: String?, senderID: String?, receiverID: String?) {
        this.message = message
        this.senderID = senderID
        this.receiverID = receiverID
    }
}*/
class Message {
    var senderId: String? = null
    var message: String? = null


    constructor() // Default constructor required for Firebase

    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }
}

