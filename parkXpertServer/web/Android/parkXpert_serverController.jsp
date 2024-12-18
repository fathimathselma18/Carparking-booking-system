<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDate"%>
<%@page import="DbConnection.DBConnection"%>
<%
   

    DBConnection con = new DBConnection();
    String key = request.getParameter("key").trim();
    System.out.println(key);


//////////////////////////////////////////////////user Register/////////////////////////////////////////////////
      if (key.equals("userRegister")) {
        String NAME = request.getParameter("name");
        String ADDRESS = request.getParameter("address");
        String NUMBER = request.getParameter("phone");
        String EMAIL = request.getParameter("email");
        String PASSWORD = request.getParameter("pass");
        String AGE = request.getParameter("age");
      
      

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateStr = currentDate.format(formatter);

        String insertQry = "SELECT COUNT(*) FROM `userregister` WHERE `email`='" + EMAIL + "' OR `mobile`='" + NUMBER + "'";
        System.out.println(insertQry);
        System.out.println("helooooooo");

        Iterator it = con.getData(insertQry).iterator();
        System.out.println("heloooooooooooooooooo");
        if (it.hasNext()) {
            Vector vec = (Vector) it.next();
            int max_vid = Integer.parseInt(vec.get(0).toString());
            System.out.println(max_vid);

            System.out.println("heloooooooooooooooooo");
            if (max_vid == 0) {
                String qry1 = "INSERT INTO`userregister`(`name`,`age`,`address`,`mobile`,`email`,`joinDate`,`status`) VALUES('" + NAME + "','" + AGE + "','" + ADDRESS + "','" + NUMBER + "','" + EMAIL + "','" + currentDateStr + "','1')";
                String qry2 = "INSERT INTO `login`(`registration_id`,`email`,`password`,`type`,`status`) VALUES((SELECT MAX(user_id)FROM `userregister`),'" + EMAIL + "','" + PASSWORD + "','USER','1')";
                System.out.println(qry1 + "\n" + qry2);

                if (con.putData(qry1) > 0 && con.putData(qry2) > 0) {

                    System.out.println("Registerd");
                    out.println("Registerd");

                } else {
                    System.out.println("Registration failed");
                    out.println("Registration failed");
                }

            } else {
                System.out.println("Already Exist");
                out.println("Already Exist");
            }
        } else {
            out.println("failed");
        }

    }


////////////////////////////////////////////////////////lgin//////////////////////////////////////////////////////////

        if (key.trim().equals("login")) {
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");

        String LoginQry = "SELECT * FROM login WHERE `email`='" + email + "' AND `password`='" + pass + "' and status='1'";
        System.out.println(LoginQry);
        Iterator i = con.getData(LoginQry).iterator();

        if (i.hasNext()) {
            Vector v = (Vector) i.next();
            out.println(v.get(1) + "#" + v.get(4) + "#");
            System.out.println(v.get(1) + "#" + v.get(4));
        } else {
            out.println("failed");
        }
    }
        
////////////////////////////////////////////////////getUserName/////////////////////////////////////////////////////////////////


      if (key.trim().equals("getUsername")) {
        String USER_ID = request.getParameter("login_id");

        String LoginQry = "SELECT * FROM `userregister` WHERE `user_id`='"+USER_ID+"'";
        System.out.println(LoginQry);
        Iterator i = con.getData(LoginQry).iterator();

        if (i.hasNext()) {
            Vector v = (Vector) i.next();
            out.println(v.get(1) + "#" + v.get(6) + "#");
            System.out.println(v.get(1) + "#" + v.get(6));
        } else {
            out.println("failed");
        }
    }


///////////////////////////////////////////////////////////addbooking//////////////////////////////////////////////////////////////


        if (key.equals("addParkingRequest")) {
        String user_id = request.getParameter("login_id");
        String SLOTNUMBER = request.getParameter("SLOTNUMBER");
        String VEHICLETYPE = request.getParameter("VEHICLETYPE");
        String DRIVINGLICENS = request.getParameter("DRIVINGLICENS");
        String PARKTIME = request.getParameter("PARKTIME");
        String Status="requested";
        String QrStatus="not scanned";
        String PaymentStatus="not payed";

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateStr = currentDate.format(formatter);

        String insertQry = "INSERT INTO `slot_booking`(`vehicletype`,`licenceNumber`,`slot_number`,`parking_time`,`status`,`user_id`,qr_status,payment_status)VALUES('"+VEHICLETYPE+"','"+DRIVINGLICENS+"','"+SLOTNUMBER+"','"+PARKTIME+"','"+Status+"','"+user_id+"','"+QrStatus+"','"+PaymentStatus+"');";
        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {

            out.println("saved");
        } else {
            out.println("failed");
        }

    }
        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (key.equals("getParkingRequest")) {
        System.out.println("heloo");
        String USER_ID = request.getParameter("login_id");
        String SlotId = request.getParameter("SLOTNUMBER");
        
        String qry = "SELECT `slot_booking`.`status` FROM `slot_booking` WHERE `slot_booking`.`user_id`='"+USER_ID+"' AND `slot_booking`.`slot_number`='"+SlotId+"'";
        System.out.println("qry=" + qry);
        Iterator i = con.getData(qry).iterator();

        if (i.hasNext()) {
            Vector v = (Vector) i.next();
            out.println(v.get(0));
            System.out.println(v.get(0));
        } else {
            out.println("failed");
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////addBookingdetails//////////////////////////////////////////////////////////////////////////
    
        if (key.equals("getSlotStatus")) {
        System.out.println("heloo");
        String qry = "SELECT `status`, `slot_number` FROM `slot_booking`";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                 obj.put("slotNumber", vector.get(1).toString().trim());
                obj.put("bookingStatus", vector.get(0).toString().trim());
              
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }

//////////////////////////////////////////////////view UserRequests/////////////////////////////////////////////////////////
   
        if (key.equals("viewUsertoAdmin")) {
        System.out.println("heloo");
    
        String qry = "SELECT * FROM `userregister` GROUP BY `userregister`.`user_id`";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("user_id", vector.get(0).toString().trim()); 
                obj.put("UserName", vector.get(1).toString().trim());              
                obj.put("userAge", vector.get(2).toString().trim());
                obj.put("userAddres", vector.get(3).toString().trim());
                obj.put("userMobile", vector.get(4).toString().trim());
                obj.put("userEmail", vector.get(5).toString().trim());
                obj.put("userJoindate", vector.get(6).toString().trim());
             
            
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
        
    

//////////////////////////////////////////////////view UserRequests/////////////////////////////////////////////////////////
   
        if (key.equals("viewUserRequeststoAdmin")) {
        System.out.println("heloo");
    
        String qry = "SELECT `slot_booking`.* ,`userregister`.* FROM `slot_booking`,`userregister` WHERE `slot_booking`.`user_id`=`userregister`.`user_id`  GROUP BY `slot_booking`.`booking_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                
               obj.put("booking_id", vector.get(0).toString().trim()); 
                obj.put("vehicleType", vector.get(1).toString().trim());              
                obj.put("licenceNumber", vector.get(2).toString().trim());
                obj.put("slotNumber", vector.get(3).toString().trim());
                obj.put("parkingTime", vector.get(4).toString().trim());
                obj.put("bookingStatus", vector.get(5).toString().trim());
                obj.put("user_id", vector.get(9).toString().trim()); 
                obj.put("UserName", vector.get(10).toString().trim());              
                obj.put("userAge", vector.get(11).toString().trim());
                obj.put("userAddres", vector.get(12).toString().trim());
                obj.put("userMobile", vector.get(13).toString().trim());
                obj.put("userEmail", vector.get(14).toString().trim());
                obj.put("userJoindate", vector.get(15).toString().trim());
                obj.put("QrStatus", vector.get(7).toString().trim());
                obj.put("paymentStatus", vector.get(8).toString().trim());
                obj.put("requestType", "viewAdmin");
            
            
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
        






/////////////////////////////////////////////////////////addQr////////////////////////////////////////////////////////////////

  if (key.equals("addQrCode")) {
    
        String user_id = request.getParameter("user_id");
        String reqId = request.getParameter("booking_id");
        String QrCode = request.getParameter("QrCode");
        
        String insertQry = "INSERT INTO `qr_code`(`user_id`,`booking_id`,`qr_code`)VALUES('"+user_id+"','"+reqId+"','"+QrCode+"')";
         String insertQry1 = "UPDATE `slot_booking` SET `status`='Accepted' WHERE `booking_id`='"+reqId+"'";
      
        if (con.putData(insertQry) > 0 &&con.putData(insertQry1) > 0  )
        {
            out.println("saved");
        } else {
            out.println("failed");
        }

    }
  
///////////////////////////////////////////////////////REquests to user///////////////////////////////////////////////////////////////////////
   

        if (key.equals("viewUserRequests")) {
        System.out.println("heloo");
        String user_id= request.getParameter("id");
    
        String qry = "SELECT `slot_booking`.* ,`userregister`.* FROM `slot_booking`,`userregister` WHERE `slot_booking`.`user_id`=`userregister`.`user_id` AND `slot_booking`.`user_id`='"+user_id+"' GROUP BY `slot_booking`.`booking_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                
                obj.put("booking_id", vector.get(0).toString().trim()); 
                obj.put("vehicleType", vector.get(1).toString().trim());              
                obj.put("licenceNumber", vector.get(2).toString().trim());
                obj.put("slotNumber", vector.get(3).toString().trim());
                obj.put("parkingTime", vector.get(4).toString().trim());
                obj.put("bookingStatus", vector.get(5).toString().trim());
                obj.put("user_id", vector.get(9).toString().trim()); 
                obj.put("UserName", vector.get(10).toString().trim());              
                obj.put("userAge", vector.get(11).toString().trim());
                obj.put("userAddres", vector.get(12).toString().trim());
                obj.put("userMobile", vector.get(13).toString().trim());
                obj.put("userEmail", vector.get(14).toString().trim());
                obj.put("userJoindate", vector.get(15).toString().trim());
                obj.put("QrStatus", vector.get(7).toString().trim());
                obj.put("paymentStatus", vector.get(8).toString().trim());
                obj.put("requestType", "viewUser");
            
            
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
        

////////////////////////////////getQr////////////////////////////////////////////////////////////////////////////////////////

    if (key.equals("getQr")) {
        System.out.println("heloo");
        String id = request.getParameter("id");
        String qry = "SELECT * FROM `qr_code` WHERE `booking_id`='"+id+"'";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("Qrcode", vector.get(3).toString().trim());   
                obj.put("booking_id", vector.get(2).toString().trim()); 
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
    
/////////////////////////////////////////updatwe passs /////////////////////////////////////////////////////////////


if (key.equals("updatePass")) {
        String id = request.getParameter("id");
        String insertQry = "UPDATE `slot_booking` SET  `qr_status`='Scanned' WHERE `booking_id`='"+id+"'";
           String insertQry1 = "UPDATE `slot_booking` SET  status='Available' WHERE `booking_id`='"+id+"'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0 && con.putData(insertQry1) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }


////////////////////////////////////////////////add amount////////////////////////////////////////////////////////////////


  if (key.equals("AddAmount")) {
    
        String amount = request.getParameter("amount");
   
        
        String insertQry = "UPDATE  `sloteprice`SET `price`='"+amount+"'";
    
        if (con.putData(insertQry) > 0  )
        {
            out.println("saved");
        } else {
            out.println("failed");
        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   if (key.trim().equals("getPrice")) {
        
        String LoginQry = "select * from `sloteprice`";
        System.out.println(LoginQry);
        Iterator i = con.getData(LoginQry).iterator();

        if (i.hasNext()) {
            Vector v = (Vector) i.next();
            out.println(v.get(0) );
            System.out.println(v.get(0) );
        } else {
            out.println("failed");
        }
    }
   

////////////////////////////////////////////////////////////////addPaymrmt/////////////////////////////////////////////////////

    if (key.equals("addbankpayment")) {
        String USER_ID = request.getParameter("user_id");
        String BOOKING_ID = request.getParameter("BookingId");
        String card_num = request.getParameter("card_num");
        String cvv = request.getParameter("cvv");
        String ac_num = request.getParameter("ac_num");
        String ex_date = request.getParameter("ex_date");
        String price = request.getParameter("price");
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateStr = currentDate.format(formatter);
        
        String insertQry = "INSERT INTO `bankpayment`(`user_id`,`booking_id`,`card_number`,`cvv`,`account_number`,`expiry_date`,`price`,`paymentDate`)VALUES('"+USER_ID+"','"+BOOKING_ID+"','"+card_num+"','"+cvv+"','"+ac_num+"','"+ex_date+"','"+price+"','"+currentDateStr+"');";
        String updateQry = "UPDATE `slot_booking` SET `payment_status`='payed' WHERE `booking_id`='"+BOOKING_ID+"'";
        System.out.println(insertQry);
        if (con.putData(insertQry) > 0 && con.putData(updateQry) > 0) {

            out.println("saved");
        } else {
            out.println("failed");
        }

    }
    


 
////////////////////////////////////////////////////payment detauils to /////////////////////////////////////////////

    if (key.equals("ViewBankingPaymentDetails")) {
       
        System.out.println("heloo");
        String qry = "SELECT `bankpayment`.* ,`userregister`.`name` FROM `bankpayment`,`userregister` WHERE `bankpayment`.`user_id`=`userregister`.`user_id` GROUP BY `bankpayment`.`bankPaymentId` DESC";
         System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("paymentId", vector.get(1).toString().trim());
                obj.put("paymentPrice", vector.get(7).toString().trim());
                obj.put("cardNumber", vector.get(3).toString().trim());
                obj.put("cvv", vector.get(4).toString().trim());
                obj.put("expiryDate", vector.get(6).toString().trim());
                obj.put("accountNumber", vector.get(5).toString().trim());
                obj.put("paymentDate", vector.get(8).toString().trim());
                obj.put("UserName", vector.get(9).toString().trim());
              
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }
   

//////////////////////////////////////////////////////////////////add feedback////////////////////////////////////////////

                
    if (key.equals("addFeedBack")) {    
        
             String user_id = request.getParameter("uid");
             String booking_id = request.getParameter("booking_id");
              String subject = request.getParameter("subject");
             String rating = request.getParameter("rating");
             String description = request.getParameter("description");
     
        
       String insertQry = "INSERT INTO `feedback`(`user_id`,`booking_id`,`subject`,`description`,`rating`)VALUES('"+user_id+"','"+booking_id+"','"+subject+"','"+description+"','"+rating+"')";
       System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {

            out.println("saved");
        } else {
            out.println("failed");
        }

    }
    

/////////////////////////////////////////////////////getFeedback/////////////////////////////////////////////////
        if (key.equals("getFeedback")) {
        System.out.println("heloo");
        String qry = "SELECT `feedback`.* ,`userregister`.`name`,`slot_booking`.`slot_number` FROM `feedback`,`userregister`,`slot_booking` WHERE `feedback`.`user_id`=`userregister`.`user_id` AND  `feedback`.`booking_id`=`slot_booking`.`booking_id` GROUP BY `feedback`.`feedback_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("UserName", vector.get(6).toString().trim());
                obj.put("slotNumber", vector.get(7).toString().trim());
                obj.put("description", vector.get(4).toString().trim());
                obj.put("rating", vector.get(5).toString().trim());
            
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
%>

