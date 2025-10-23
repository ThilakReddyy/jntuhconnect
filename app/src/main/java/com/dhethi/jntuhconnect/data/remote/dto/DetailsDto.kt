package com.dhethi.jntuhconnect.data.remote.dto


import com.dhethi.jntuhconnect.domain.model.Details
import com.google.gson.annotations.SerializedName

data class DetailsDto(
    @SerializedName("collegeCode")
    val collegeCode: String,
    @SerializedName("fatherName")
    val fatherName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("rollNumber")
    val rollNumber: String,
    @SerializedName("branch")
    val branch: String

)

fun DetailsDto.toDetails(): Details {
    return Details(
        name = name,
        collegeCode = collegeCode,
        fatherName = fatherName,
        rollNumber = rollNumber,
        branch = branch
    )

}