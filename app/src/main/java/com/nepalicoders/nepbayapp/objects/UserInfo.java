package com.nepalicoders.nepbayapp.objects;

/**
 * Created by kshitij on 12/9/15.
 */
public class UserInfo {
    String id;
    String username;
    String firstName;
    String lastName;
    String profileUrl;
    String country;
    String address1;
    String city;
    String zip;
    String address2;
    String email;
    String phone;
    String fax;
    String state;
    String cityId;
    String districtId;
    String vdc;
    String wardNo;
    String locality;
    String houseNo;
    String mobileNo;

    //For storing shipping info
    /*String shippingId;
    String shippingUsername;
    String shippingFirstName;
    String shippingLastName;
    String shippingCountry;
    String shippingAddress1;
    String shippingAddress2;
    String shippingCity;
    String shippingZip;
    String shippingOrderNumber;
    String shippingState;
    String shippingCityId;
    String shippingDistrictId;
    String shippingVdc;
    String shippingWardNo;
    String shippingLocality;
    String shippingHouseNo;
    String shippingMobileNo;
    String shippingExtraMobileNo;*/

    String totalOrders;
    String approvedOrders;
    String rejectedOrders;
    String pendingOrders;

    public UserInfo(){
        /*id="-";
        username="-";
        firstName="-";
        lastName="-";
        country="-";
        address1="-";
        city="-";
        zip="-";
        address2="-";
        email="-";
        phone="-";
        fax="-";
        state="-";
        cityId="-";
        districtId="-";
        vdc="-";
        wardNo="-";
        locality="-";
        houseNo="-";
        mobileNo="-";
        shippingId="-";
        shippingUsername="-";
        shippingFirstName="-";
        shippingLastName="-";
        shippingCountry="-";
        shippingAddress1="-";
        shippingAddress2="-";
        shippingCity="-";
        shippingZip="-";
        shippingOrderNumber="-";
        shippingState="-";
        shippingCityId="-";
        shippingDistrictId="-";
        shippingVdc="-";
        shippingWardNo="-";
        shippingLocality="-";
        shippingHouseNo="-";
        shippingMobileNo="-";
        shippingExtraMobileNo="-";
        totalOrders="-";
        approvedOrders="-";
        rejectedOrders="-";
        pendingOrders="-";*/
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getVdc() {
        return vdc;
    }

    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /*public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingUsername() {
        return shippingUsername;
    }

    public void setShippingUsername(String shippingUsername) {
        this.shippingUsername = shippingUsername;
    }

    public String getShippingFirstName() {
        return shippingFirstName;
    }

    public void setShippingFirstName(String shippingFirstName) {
        this.shippingFirstName = shippingFirstName;
    }

    public String getShippingLastName() {
        return shippingLastName;
    }

    public void setShippingLastName(String shippingLastName) {
        this.shippingLastName = shippingLastName;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        this.shippingAddress1 = shippingAddress1;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        this.shippingAddress2 = shippingAddress2;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingZip() {
        return shippingZip;
    }

    public void setShippingZip(String shippingZip) {
        this.shippingZip = shippingZip;
    }

    public String getShippingOrderNumber() {
        return shippingOrderNumber;
    }

    public void setShippingOrderNumber(String shippingOrderNumber) {
        this.shippingOrderNumber = shippingOrderNumber;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingCityId() {
        return shippingCityId;
    }

    public void setShippingCityId(String shippingCityId) {
        this.shippingCityId = shippingCityId;
    }

    public String getShippingDistrictId() {
        return shippingDistrictId;
    }

    public void setShippingDistrictId(String shippingDistrictId) {
        this.shippingDistrictId = shippingDistrictId;
    }

    public String getShippingVdc() {
        return shippingVdc;
    }

    public void setShippingVdc(String shippingVdc) {
        this.shippingVdc = shippingVdc;
    }

    public String getShippingWardNo() {
        return shippingWardNo;
    }

    public void setShippingWardNo(String shippingWardNo) {
        this.shippingWardNo = shippingWardNo;
    }

    public String getShippingLocality() {
        return shippingLocality;
    }

    public void setShippingLocality(String shippingLocality) {
        this.shippingLocality = shippingLocality;
    }

    public String getShippingHouseNo() {
        return shippingHouseNo;
    }

    public void setShippingHouseNo(String shippingHouseNo) {
        this.shippingHouseNo = shippingHouseNo;
    }

    public String getShippingMobileNo() {
        return shippingMobileNo;
    }

    public void setShippingMobileNo(String shippingMobileNo) {
        this.shippingMobileNo = shippingMobileNo;
    }

    public String getShippingExtraMobileNo() {
        return shippingExtraMobileNo;
    }

    public void setShippingExtraMobileNo(String shippingExtraMobileNo) {
        this.shippingExtraMobileNo = shippingExtraMobileNo;
    }*/

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getApprovedOrders() {
        return approvedOrders;
    }

    public void setApprovedOrders(String approvedOrders) {
        this.approvedOrders = approvedOrders;
    }

    public String getRejectedOrders() {
        return rejectedOrders;
    }

    public void setRejectedOrders(String rejectedOrders) {
        this.rejectedOrders = rejectedOrders;
    }

    public String getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(String pendingOrders) {
        this.pendingOrders = pendingOrders;
    }
}
