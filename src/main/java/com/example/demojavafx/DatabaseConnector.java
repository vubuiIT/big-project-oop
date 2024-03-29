package com.example.demojavafx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    private List<DatabaseChangeListener> listeners = new ArrayList<>();
    private static Connection connection;
    private final String databaseName;


    public DatabaseConnector() {
        this.databaseName = "database.db";
    }
    public void addDatabaseChangeListener(DatabaseChangeListener listener) {
        listeners.add(listener);
    }

    public void removeDatabaseChangeListener(DatabaseChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (DatabaseChangeListener listener : listeners) {
            listener.onDatabaseChange();
        }
    }
    public void connect() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);

            System.out.println("Connected to the database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }
    public List<Question> getQuestionsFromCategory(int categoryId) {
        List<Question> questions = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM Question WHERE category_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameter value
            statement.setInt(1, categoryId);

            // Execute the SQL statement
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int questionId = resultSet.getInt("id");
                String text = resultSet.getString("text");
                String name = resultSet.getString("name");
                String mediaName = resultSet.getString("media_name");
                byte[] media = resultSet.getBytes("media");
                float mark = resultSet.getFloat("mark");

                Question question = new Question(questionId, categoryId, text, name, media, mark, mediaName);
                questions.add(question);
            }

        } catch (SQLException e) {
            System.err.println("Failed to get questions from category: " + e.getMessage());
        }

        return questions;
    }
    public int amountQuestionsFromCategory(int categoryId) {
        List<Question> questions = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM Question WHERE category_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameter value
            statement.setInt(1, categoryId);

            // Execute the SQL statement
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int questionId = resultSet.getInt("id");
                String text = resultSet.getString("text");
                String name = resultSet.getString("name");
                String mediaName = resultSet.getString("media_name");
                byte[] media = resultSet.getBytes("media");
                float mark = resultSet.getFloat("mark");

                Question question = new Question(questionId, categoryId, text, name, media, mark, mediaName);
                questions.add(question);
            }

        } catch (SQLException e) {
            System.err.println("Failed to get questions from category: " + e.getMessage());
        }

        return questions.size();
    }
    public String addCategory(int parentId, String info, String name) {
        try {
            // Prepare SQL statement
            String sql = "INSERT INTO Category (parent_id, info, name) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameter values
            statement.setInt(1, parentId);
            statement.setString(2, info);
            statement.setString(3, name);

            // Execute the SQL statement
            statement.executeUpdate();

            return "Category added successfully!";

        } catch (SQLException e) {
           return ("Failed to add category: " + e.getMessage());
        }
    }
    public String addNewCategoryWithId(int id, int parentId, String info, String name) {
        try {
            // Kiểm tra xem id đã tồn tại trong bảng Category chưa
            String checkQuery = "SELECT id FROM Category WHERE id = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, id);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                return ("Category ID already exists");
            }
            else {

                // Thực hiện thêm bản ghi mới vào bảng Category
                String insertQuery = "INSERT INTO Category (id, parent_id, info, name) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, id);
                insertStatement.setInt(2, parentId);
                insertStatement.setString(3, info);
                insertStatement.setString(4, name);
                insertStatement.executeUpdate();
                return ("Category added successfully.");
            }
        } catch (SQLException e) {
            return ("Failed to add category: " + e.getMessage());
        }
    }
    public static Category getCategory(int CategoryId) {
        List<Category> categories = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM Category WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, CategoryId);
            // Execute the SQL statement
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int parentId = resultSet.getInt("parent_id");
                String info = resultSet.getString("info");
                String name = resultSet.getString("name");
                // Create a new Category object
                //if(parentId!=CategoryId)
                return new Category(CategoryId, parentId, info, name);
                   // Category category = new Category(CategoryId, parentId, info, name);

                // Add the category to the list
                //categories.add(category);
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Failed to retrieve categories: " + e.getMessage());
        }

        //return categories;
        return new Category(CategoryId,-1,"","");
    }

    public List<Category> getCategories(int CategoryId) {
        List<Category> categories = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM Category";
            PreparedStatement statement = connection.prepareStatement(sql);
            // Execute the SQL statement
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int Id = resultSet.getInt("id");
                int parentId = resultSet.getInt("parent_id");
                String info = resultSet.getString("info");
                String name = resultSet.getString("name");
                // Create a new Category object
                    Category category = new Category(Id, parentId, info, name);
                // Add the category to the list

                if(parentId==CategoryId) {
                    categories.add(category);
                    categories.addAll(getCategories(Id));
                }
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Failed to retrieve categories: " + e.getMessage());
        }
        return categories;
    }
    public void clearQuiz(int quizId){
        try {
            String sql = "DELETE FROM QuizQues WHERE quiz_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quizId);
            statement.executeUpdate();
            System.out.println("Question deleted successfully from quiz" + quizId);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int addQuestion(int categoryId, String questionText, String questionName, byte[] questionMedia, String mediaName, float questionMark) {
        int questionId = -1; // Giá trị mặc định nếu thất bại

        try {
            // Chuẩn bị câu lệnh SQL với các tham số giữ chỗ
            String sql = "INSERT INTO Question (category_id, text, name, media, media_name, mark) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Thiết lập giá trị cho các tham số
            statement.setInt(1, categoryId);
            statement.setString(2, questionText);
            statement.setString(3, questionName);
            statement.setBytes(4, questionMedia);
            statement.setString(5, mediaName);
            statement.setFloat(6, questionMark);

            // Thực thi câu lệnh SQL
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Lấy các khóa tự tạo (trong trường hợp này, question ID)
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    questionId = generatedKeys.getInt(1);
                    System.out.println("Thêm câu hỏi thành công. ID: " + questionId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Thêm câu hỏi thất bại: " + e.getMessage());
        }

        return questionId;
    }

        public int addChoice(int questionId, float grade, byte[] picData, String choiceText, String picName) {
        int choiceId = -1; // Giá trị mặc định nếu thất bại

        try {
            // Chuẩn bị câu lệnh SQL với các placeholder tham số
            String sql = "INSERT INTO Choice (question_id, grade, pic, text, pic_name) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Đặt giá trị cho các tham số
            statement.setInt(1, questionId);
            statement.setFloat(2, grade);
            statement.setBytes(3, picData);
            statement.setString(4, choiceText);
            statement.setString(5, picName);

            // Thực thi câu lệnh SQL
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Lấy khóa tự sinh (id) được tạo ra
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    choiceId = generatedKeys.getInt(1);
                    System.out.println("Choice added successfully. ID: " + choiceId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to add choice: " + e.getMessage());
        }

        return choiceId;
    }
    public byte[] getPicChoiceData(int choiceId) {
        byte[] picData = null;
        String sql = "SELECT pic FROM Choice WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, choiceId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                picData = resultSet.getBytes("pic");
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return picData;
    }
    public byte[] getMediaData(int questionId) {
        byte[] mediaData = null;
        String sql = "SELECT media FROM Question WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                mediaData = resultSet.getBytes("media");
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mediaData;
    }
    public void addQuesWithId(int questionId, int categoryId, String questionText, String questionName, byte[] questionMedia,  String mediaName, float questionMark) {
        String sql = "INSERT INTO Question (id, category_id, text, name, media, media_name, mark) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            statement.setInt(2, categoryId);
            statement.setString(3, questionText);
            statement.setString(4, questionName);
            statement.setBytes(5, questionMedia);
            statement.setString(6, mediaName);
            statement.setFloat(7, questionMark);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Question with ID " + questionId + " added successfully.");
            } else {
                System.out.println("Failed to add question with ID " + questionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteQues(int questionId) {
        String sql = "DELETE FROM Question WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Question with ID " + questionId + " deleted successfully.");
            } else {
                System.out.println("Question with ID " + questionId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteChoice(int choiceId) {
        String sql = "DELETE FROM Choice WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, choiceId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Choice with ID " + choiceId + " deleted successfully.");
            } else {
                System.out.println("Choice with ID " + choiceId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Choices> getChoicesFromQuestion(int questionId) {
        List<Choices> choices = new ArrayList<>();
        String sql = "SELECT * FROM Choice WHERE question_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int questionIdResult = resultSet.getInt("question_id");
                float grade = resultSet.getFloat("grade");
                byte[] picData = resultSet.getBytes("pic");
                String text = resultSet.getString("text");
                String picName = resultSet.getString("pic_name");

                Choices choice = new Choices(id, questionIdResult, grade, picData, text,picName);
                choices.add(choice);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return choices;
    }
    public List<Quiz> getQuiz() {
        List<Quiz> quizzes = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Quiz");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int displayDescription = resultSet.getInt("display_description");
                int enableTimeLimit = resultSet.getInt("enable_time_limit");
                int timeLimit = resultSet.getInt("time_limit");
                int shuffle = resultSet.getInt("shuffle");

                Quiz quiz = new Quiz(id, name, description, displayDescription, enableTimeLimit, timeLimit, shuffle);
                quizzes.add(quiz);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;
    }
    public void changeShuffleValue(int quizId, int newShuffleValue) {
        try {
            // Tạo prepared statement với câu lệnh UPDATE
            String sql = "UPDATE Quiz SET shuffle = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Thiết lập giá trị cho các tham số của prepared statement
            statement.setInt(1, newShuffleValue);
            statement.setInt(2, quizId);

            // Thực thi câu lệnh UPDATE
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Shuffle value changed successfully for Quiz ID: " + quizId);
            } else {
                System.out.println("Quiz ID not found or shuffle value was already set to the new value.");
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println("Failed to change shuffle value: " + e.getMessage());
        }
    }
    public void addQuiz(String name, String description, Integer displayDes, Integer enableTimeLimit, Integer timeLimit, Integer shuffle) {
        try {
            // Tạo prepared statement với câu lệnh INSERT INTO
            String sql = "INSERT INTO Quiz (name, description, display_description, enable_time_limit, time_limit, shuffle) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Thiết lập giá trị cho các tham số của prepared statement
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setInt(3, displayDes);
            statement.setInt(4, enableTimeLimit);
            statement.setInt(5, timeLimit);
            statement.setInt(6, shuffle); // Thêm giá trị của cột "shuffle"

            // Thực thi câu lệnh INSERT INTO
            statement.executeUpdate();

            System.out.println("Quiz added successfully.");

            statement.close();
        } catch (SQLException e) {
            System.err.println("Failed to add quiz: " + e.getMessage());
        }
    }
    // Them cac question duoc chon vao trong quiz
    public void addQuesToQuiz(int quesId, int quizId){
        try {
            String sql = "INSERT INTO QuizQues (ques_id, quiz_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quesId);
            statement.setInt(2, quizId);
            statement.executeUpdate();
            System.out.println("Question added successfully to Quiz");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestionsFromQuiz(int quizId) {
        List<Question> questions = new ArrayList<>();
        List<Integer> tmpIds = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM QuizQues WHERE quiz_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameter value
            statement.setInt(1, quizId);

            // Execute the SQL statement
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int tmpId = resultSet.getInt("ques_id");
                tmpIds.add(tmpId);
            }

            for (int tmpId : tmpIds) {
                String sql1 = "SELECT * FROM Question WHERE id = ?";
                PreparedStatement statement1 = connection.prepareStatement(sql1);

                // Set the parameter value
                statement1.setInt(1, tmpId);

                // Execute the SQL statement
                ResultSet resultSet1 = statement1.executeQuery();

                // Process the result set
                while (resultSet1.next()) {
                    int categoryId = resultSet1.getInt("category_id");
                    String text = resultSet1.getString("text");
                    String name = resultSet1.getString("name");
                    String mediaName = resultSet1.getString("media_name");
                    byte[] media = resultSet1.getBytes("media");
                    float mark = resultSet1.getFloat("mark");

                    Question question = new Question(tmpId, categoryId, text, name, media, mark, mediaName);
                    questions.add(question);


                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to get questions from quiz: " + e.getMessage());
        }
        return questions;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to disconnect from the database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args)  {
        // Create an instance of DatabaseConnector
        DatabaseConnector connector = new DatabaseConnector();

        // Connect to the database
        connector.connect();
        connector.getQuiz();
        List<Quiz> quizzes = connector.getQuiz();
        // Print information of each quiz
        for (Quiz quiz : quizzes) {
            System.out.println("Quiz ID: " + quiz.getId());
            System.out.println("Name: " + quiz.getName());
            System.out.println("Description: " + quiz.getDescription());
            System.out.println("Display Description: " + quiz.getDisplayDescription());
            System.out.println("Enable Time Limit: " + quiz.getEnableTimeLimit());
            System.out.println("Time Limit: " + quiz.getTimeLimit());
            System.out.println();
        }

//         Perform database operations
//         Demo add question
//        int idQuestion = connector.addQuestion(3,"Lá cây có màu xanh","Vì sao lá cây có màu xanh","", 1.25F);
//        System.out.print("ID question vua tao " + idQuestion);
        /*Demo add category*/
        //connector.addCategory(4,"Vippro","Vippro");
        /*Demo get all category*/
//        List<Category> categories = connector.getCategories();
//        connector.addChoice(1, 3.5f, "pic2.png", "Choice 4 Text");;
        // Process the categories
//        for (Category category : categories) {
//            System.out.println("Category ID: " + category.getId(  ));
//            System.out.println("Parent ID: " + category.getParentId());
//            System.out.println("Info: " + category.getInfo());
//            System.out.println("Name: " + category.getName());
//            System.out.println("---------------------");
//        }
////       Demo get question from category
//        int categoryId = 3; // Example category ID
//        List<Question> questions = connector.getQuestionsFromCategory(categoryId);
//
//        // Print the questions
//        for (Question question : questions) {
//            System.out.println("Question ID: " + question.getId());
//            System.out.println("Category ID: " + question.getCategoryId());
//            System.out.println("Question Text: " + question.getText());
//            System.out.println("Question Name: " + question.getName());
//            System.out.println("Question Media:         " + question.getMedia());
//            System.out.println("Question Mark: " + question.getMark());
//            System.out.println("----------------------");
//        }

//        List<Choices> choices = connector.getChoicesFromQuestion(1);
//        for (Choices choice : choices) {
//            System.out.println("Choice ID: " + choice.getId());
//            System.out.println("Question ID: " + choice.getQuestionId());
//            System.out.println("Choice Grade: " + choice.getGrade());
//            System.out.println("Choice Pic: " + choice.getPic());
//
//            System.out.println("----------------------");
//        }
//        // Disconnect from the database

        connector.disconnect();
    }
}
