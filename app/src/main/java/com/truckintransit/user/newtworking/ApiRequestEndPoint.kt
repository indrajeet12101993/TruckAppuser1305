package com.truckintransit.user.newtworking

import com.truckintransit.user.pojo.ResponseFromServerForBookInitil
import com.truckintransit.user.pojo.ResponseFromServerGeneric
import com.truckintransit.user.pojo.otpVerify.ResponseFromServerOtpVerify
import com.truckintransit.user.pojo.paytm.ResponseFromPaytmChecksum
import com.truckintransit.user.pojo.service.ResponseFromServerForServiceList
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiRequestEndPoint {

    @FormUrlEncoded
    @POST("User/login")
    fun postServerUserPhoneNumber(@Field("phone") phone: String,@Field("fcm_id") fcm_id: String): Observable<ResponseFromServerGeneric>


    @FormUrlEncoded
    @POST("User/otpcheck")
    fun postServerUserOtpVerify(@Field("otp") otp: String): Observable<ResponseFromServerOtpVerify>



    @GET("User/user_notification")
    fun postServerNotification(): Observable<ResponseFromServerGeneric>


    @FormUrlEncoded
    @POST("User/services")
    fun getServiceList(@Field("lat") lat: String,@Field("longg") longg: String): Observable<ResponseFromServerForServiceList>


    @FormUrlEncoded
    @POST("User/updateuser")
    fun postUser(@Field("user_id") user_id: String,
                 @Field("name") name: String,
                 @Field("email") email: String,
                 @Field("refferal") refferal: String,
                 @Field("status") status: String): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/bank_detail")
    fun postServerBankRegistrstion(@Field("user_id") user_id: String,
                                        @Field("adhar") adhar: String,
                                        @Field("pan") pan: String,
                                        @Field("acc_no") acc_no: String,
                                        @Field("ifsc") ifsc: String,
                                        @Field("type") type: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("http://technowhizzit.com/Truckintransit/Api/paytmpost")
    fun postPaytm(@Field("ORDER_ID") ORDER_ID: String,
                                   @Field("CUST_ID") CUST_ID: String,
                                   @Field("INDUSTRY_TYPE_ID") INDUSTRY_TYPE_ID: String,
                                   @Field("CHANNEL_ID") CHANNEL_ID: String,
                                   @Field("TXN_AMOUNT") TXN_AMOUNT: String
    ): Observable<ResponseFromPaytmChecksum>

    @FormUrlEncoded
    @POST("Api/booking")
    fun postServerBooking(@Field("customer_id") customer_id: String,
                                   @Field("serviceid") serviceid: String,
                                   @Field("pickup_loc") pickup_loc: String,
                                   @Field("pickup_lat") pickup_lat: String,
                                   @Field("pickup_longg") pickup_longg: String,
                                   @Field("pickup2") pickup2: String,
                                   @Field("pickup2_lat") pickup2_lat: String,
                                   @Field("pickup2_longgg") pickup2_longgg: String,
                                   @Field("pickup3") pickup3: String,
                                   @Field("pickup3_lat") pickup3_lat: String,
                                   @Field("pickup3_longg") pickup3_longg: String,
                                   @Field("drop_loc") drop_loc: String,
                                   @Field("drop_lat") drop_lat: String,
                                   @Field("drop_longg") drop_longg: String,
                                   @Field("dropoff2") dropoff2: String,
                                   @Field("drop2_lat") drop2_lat: String,
                                   @Field("drop2_longg") drop2_longg: String,
                                   @Field("dropoff3") dropoff3: String,
                                   @Field("drop3_lat") drop3_lat: String,
                                   @Field("drop3_longg") drop3_longg: String,
                                   @Field("truckid") truckid: String,
                                   @Field("optionshelper") optionshelper: String,
                                   @Field("vehiclebodytype") vehiclebodytype: String,
                                   @Field("vehiclefueltypeid") vehiclefueltypeid: String,
                                   @Field("vehicletypeid") vehicletypeid: String,
                                   @Field("material_type") material_type: String,
                                   @Field("coupon") coupon: String,
                                   @Field("freightpayment") freightpayment: String,
                                   @Field("payment_mode") payment_mode: String,
                                   @Field("titcredits") titcredits: String
    ): Observable<ResponseFromServerForBookInitil>


    @Multipart
    @POST("Api/user")
    fun uploadImage(@Part image: MultipartBody.Part,
                    @Part("user_id") user_id: RequestBody,
                    @Part("name") name: RequestBody,
                    @Part("email") email: RequestBody,
                    @Part("pass") pass: RequestBody,
                    @Part("type") type: RequestBody
    ): Observable<ResponseFromServerGeneric>

}