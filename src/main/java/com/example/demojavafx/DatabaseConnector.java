package com.example.demojavafx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    private Connection connection;
    private final String databaseName;


    public DatabaseConnector() {
        this.databaseName = "database.db";
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
                String media = resultSet.getString("media");
                float mark = resultSet.getFloat("mark");

                Question question = new Question(questionId, categoryId, text, name, media, mark);
                questions.add(question);
            }

        } catch (SQLException e) {
            System.err.println("Failed to get questions from category: " + e.getMessage());
        }

        return questions;
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
    public Category getCategory(int CategoryId) {
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
    public int addQuestion(int categoryId, String questionText, String questionName, String questionMedia, float questionMark) {
        int questionId = -1; // Default value in case of failure

        try {
            // Prepare SQL statement with parameter placeholders
            String sql = "INSERT INTO Question (category_id, text, name, media, mark) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set values for the parameters
            statement.setInt(1, categoryId);
            statement.setString(2, questionText);
            statement.setString(3, questionName);
            statement.setString(4, questionMedia);
            statement.setFloat(5, questionMark);

            // Execute the SQL statement
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated keys (in this case, the question ID)
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    questionId = generatedKeys.getInt(1);
                    System.out.println("Question added successfully. ID: " + questionId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to add question: " + e.getMessage());
        }

        return questionId;
    }
    public void addChoice(int questionId, float grade, String pic, String text) {
        try {
            String sql = "INSERT INTO Choice (question_id, grade, pic, text) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, questionId);
            statement.setFloat(2, grade);
            statement.setString(3, pic);
            statement.setString(4, text);
            statement.executeUpdate();
            System.out.println("Choice added successfully");
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private List<Choices> getChoicesFromQuestion(int questionId) {
        List<com.example.demojavafx.Choices> choices = new ArrayList<>();
        String sql = "SELECT * FROM Choice WHERE question_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int questionIdResult = resultSet.getInt("question_id");
                float grade = resultSet.getFloat("grade");
                String pic = resultSet.getString("pic");
                String text = resultSet.getString("text");

                com.example.demojavafx.Choices choice = new com.example.demojavafx.Choices(id, questionIdResult, grade, pic, text);
                choices.add(choice);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return choices;
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

//         Perform database operations
//         Demo add question
//        int idQuestion = connector.addQuestion(3,"Lá cây có màu xanh","Vì sao lá cây có màu xanh","", 1.25F);
//        System.out.print("ID question vua tao " + idQuestion);
        /*Demo add category*/
        connector.addCategory(4,"Vippro","Vippro");
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
