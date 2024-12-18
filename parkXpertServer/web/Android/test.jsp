

<%



<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Iterator"%>
<%@page import="DbConnection.DBConnection"%>
<%

    DBConnection con = new DBConnection();
    String key = request.getParameter("key").trim();
    System.out.println(key);

    /////////////////////////////////////user Register///////////////////////////////////////////////////////////////////
    if (key.equals("userRegister")) {
        String NAME = request.getParameter("name");
        String ADDRESS = request.getParameter("address");
        String NUMBER = request.getParameter("phone");
        String EMAIL = request.getParameter("email");
        String PASSWORD = request.getParameter("pass");
        String AGE = request.getParameter("age");
        String GENDER = request.getParameter("gender");
        //String DATE = request.getParameter("date");

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateStr = currentDate.format(formatter);

        String insertQry = "SELECT COUNT(*) FROM `user_register` WHERE `email`='" + EMAIL + "' OR `phone`='" + NUMBER + "'";
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
                String qry1 = "INSERT INTO`user_register`(`name`,`age`,`gender`,`address`,`phone`,`email`,`date_of_join`,`status`) VALUES('" + NAME + "','" + AGE + "','" + GENDER + "','" + ADDRESS + "','" + NUMBER + "','" + EMAIL + "','" + currentDateStr + "','0')";
                String qry2 = "INSERT INTO `login`(`registration_id`,`email`,`password`,`type`,`status`) VALUES((SELECT MAX(user_id)FROM `user_register`),'" + EMAIL + "','" + PASSWORD + "','USER','1')";
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

    //////////////////////////////////////////////////////login///////////////////////////////////////////////////////
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

/////////////////////////////////////////////////////view user/////////////////////////////////////////////////////////////////
    if (key.equals("viewUsers")) {
        System.out.println("heloo");
        String id = request.getParameter("reg_id").trim();
        String qry = "SELECT `user_register`.*, `login`.`status` FROM `user_register`,`login` WHERE `user_register`.`user_id`=`login`.`registration_id` AND `type`='USER' ORDER BY `user_register`.`user_id` DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("user_id", vector.get(0).toString().trim());
                obj.put("user_name", vector.get(1).toString().trim());
                obj.put("user_age", vector.get(2).toString().trim());
                obj.put("user_gender", vector.get(3).toString().trim());
                obj.put("user_address", vector.get(4).toString().trim());
                obj.put("user_phone", vector.get(5).toString().trim());
                obj.put("user_email", vector.get(6).toString().trim());
                obj.put("user_dateofJoin", vector.get(7).toString().trim());
                obj.put("user_status", vector.get(9).toString().trim());
                obj.put("getUserType", "viewUsers");
                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }

//////////////////////////////////////////////////blockUser/////////////////////////////////////////////////////////////////
    if (key.equals("BlockRequest")) {
        String id = request.getParameter("userId");
        String insertQry = "UPDATE login SET status='2' WHERE `registration_id`='" + id + "'and type='USER'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }

//////////////////////////////////////////////////accept user//////////////////////////////////////////////////////////
    if (key.equals("AcceptRequest")) {
        String id = request.getParameter("userId");
        String insertQry = "UPDATE login SET status='1' WHERE `registration_id`='" + id + "'and type='USER'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }

/////////////////////////////////////////////////////add song////////////////////////////////////////////////////////
    if (key.equals("addSong")) {
        String SONG_NAME = request.getParameter("SONG_NAME");
        String MOVIE_NAME = request.getParameter("MOVIE_NAME");
        String MUSIC_DIRECTOR = request.getParameter("MUSIC_DIRECTOR");
        String SINGER = request.getParameter("SINGER");
        String RATING = request.getParameter("RATING");
        String GENRE = request.getParameter("GENRE");
        String LYRICS = request.getParameter("LYRICS");
        String image = request.getParameter("image");
        String status = "0";

        String insertQry = "INSERT INTO `songs_lyrics`(`song`, `movie`, `music_director`, `singer`,`rating`, `genre`, `music_image`, `lyrics_chords`,`status`)VALUES('" + SONG_NAME + "','" + MOVIE_NAME + "','" + MUSIC_DIRECTOR + "','" + SINGER + "','" + RATING + "','" + GENRE + "','" + image + "','" + LYRICS + "','" + status + "')";
        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {

            out.println("saved");
        } else {
            out.println("failed");
        }

    }

//////////////////////////////////////////////////////getSongs////////////////////////////////////////////////////////////////
    if (key.equals("getSongs")) {
        System.out.println("heloo");
        String qry = "SELECT * FROM `songs_lyrics` where `songs_lyrics`.`status`=0 GROUP BY `songs_lyrics`.`song_id`DESC";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("song_id", vector.get(0).toString().trim());
                obj.put("songNAme", vector.get(1).toString().trim());
                obj.put("musicDirector", vector.get(3).toString().trim());
                obj.put("movieName", vector.get(2).toString().trim());
                obj.put("rating", vector.get(5).toString().trim());
                obj.put("MusicGenre", vector.get(6).toString().trim());
                obj.put("singerNAme", vector.get(4).toString().trim());
                obj.put("musicImage", vector.get(7).toString().trim());
                obj.put("lyrics", vector.get(8).toString().trim());

                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }

////////////////////////////////////////deletSongs//////////////////////////////////////////////////
    if (key.equals("deleteSong")) {
        String songId = request.getParameter("songId");
        String insertQry = "UPDATE `songs_lyrics` SET `songs_lyrics`.status='1' WHERE `songs_lyrics`.`song_id`='" + songId + "'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    if (key.equals("addToFavorite")) {
        String SONG_ID = request.getParameter("SONG_ID");
        String USER_ID = request.getParameter("USER_ID");
        String status = "0";

        String insertQry = "SELECT COUNT(*) FROM `favorites` WHERE `song_id`='" + SONG_ID + "' AND `user_id`='" + USER_ID + "'";

        System.out.println(insertQry);
        Iterator it = con.getData(insertQry).iterator();
        if (it.hasNext()) {
            Vector vec = (Vector) it.next();
            int max_vid = Integer.parseInt(vec.get(0).toString());
            System.out.println(max_vid);

            if (max_vid == 0) {
                String qry2 = "insert into `favorites`(`song_id`,`user_id`,`status`) values('" + SONG_ID + "','" + USER_ID + "','" + status + "');";
                System.out.println(qry2);

                if (con.putData(qry2) > 0) {

                    System.out.println("Addedd");
                    out.println("Adedd");

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

/////////////////////////////////////////////////////////getFavorites/////////////////////////////////////////////////////////////////////////
    if (key.equals("getFavorites")) {
        System.out.println("heloo");
        String USER_ID = request.getParameter("userId");
        String qry = "select `songs_lyrics`.* ,`favorites`.* from `songs_lyrics`,`favorites` where `favorites`.`song_id`=`songs_lyrics`.`song_id` and `favorites`.`user_id`='" + USER_ID + "' AND `songs_lyrics`.`status`=0";
        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("song_id", vector.get(0).toString().trim());
                obj.put("songNAme", vector.get(1).toString().trim());
                obj.put("musicDirector", vector.get(3).toString().trim());
                obj.put("movieName", vector.get(2).toString().trim());
                obj.put("rating", vector.get(5).toString().trim());
                obj.put("MusicGenre", vector.get(6).toString().trim());
                obj.put("singerNAme", vector.get(4).toString().trim());
                obj.put("musicImage", vector.get(7).toString().trim());
                obj.put("lyrics", vector.get(8).toString().trim());

                array.add(obj);
            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);

        } else {
            out.println("failed");
        }
    }

////////////////////////////////////////remove favorite//////////////////////////////////////////////////
    if (key.equals("RemoveFavorite")) {
        String songId = request.getParameter("songId");
        String userId = request.getParameter("userId");
        String insertQry = "delete from `favorites` where `user_id`='" + userId + "' AND `song_id`='" + songId + "'";

        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {
            out.println("saved");
        } else {
            out.println("failed");
        }
    }

/////////////////////////////////////////////////////addfeedback////////////////////////////////////////////////////////////////////
    if (key.equals("addFeedBack")) {
        String user_id = request.getParameter("uid");
        String song_id = request.getParameter("song_id");
        String subject = request.getParameter("subject");
        String description = request.getParameter("description");
        String rating = request.getParameter("rating");

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateStr = currentDate.format(formatter);

        String insertQry = "INSERT INTO `feedback`(`song_id`,`user_id`,`subject`,`description`,`rating`,`date`)VALUES('" + song_id + "','" + user_id + "','" + subject + "','" + description + "','" + rating + "','" + currentDateStr + "');";
        System.out.println(insertQry);
        if (con.putData(insertQry) > 0) {

            out.println("saved");
        } else {
            out.println("failed");
        }

    }

///////////////////////////////////////////////////getFeedBAck/////////////////////////////////////////////////////////////////////////////////////
    if (key.equals("getFeedBack")) {
        System.out.println("heloo");
        String qry = "SELECT `feedback`.*,`user_register`.`name` ,`songs_lyrics`.`song`FROM `feedback`,`user_register`,`songs_lyrics` WHERE `feedback`.`user_id`=`user_register`.`user_id` AND `feedback`.`song_id`=`songs_lyrics`.`song_id` GROUP BY `feedback_id`DESC";

        System.out.println("qry=" + qry);
        JSONArray array = new JSONArray();
        Iterator it = con.getData(qry).iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                Vector vector = (Vector) it.next();
                JSONObject obj = new JSONObject();
                obj.put("feedbAckID", vector.get(0).toString().trim());
                obj.put("songNAme", vector.get(8).toString().trim());
                obj.put("subject", vector.get(3).toString().trim());
                obj.put("descrption", vector.get(4).toString().trim());
                obj.put("feedbackDate", vector.get(6).toString().trim());
                obj.put("feedBAckRating", vector.get(5).toString().trim());
                obj.put("user_name", vector.get(7).toString().trim());
                array.add(obj);

            }
            out.println(array);
            System.out.println("All Data");
            System.out.println(array);
        } else {
            out.println("failed");
        }
    }
%>
















%>

