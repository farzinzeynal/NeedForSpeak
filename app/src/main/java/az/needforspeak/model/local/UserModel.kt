package az.needforspeak.model.local

data class UserModel(
    var plateNumber: String,
    var name: String,
    var surname: String,
    var phone: String?,
    var photo: String?,
    var desc: String?,
    var status: String?,
    var career: String?,
    var education: String?,
    var interests: String?
)