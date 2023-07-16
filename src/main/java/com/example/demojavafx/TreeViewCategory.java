package com.example.demojavafx;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeViewCategory {

    private static final String DB_URL = "jdbc:sqlite:database.db";
    private final TreeView<Category> treeView;
    private final ChoiceBox<String> choiceBoxCategory;

    public int idTreeViewChoice = -1;
    public TreeViewCategory(TreeView <Category> inputTreeView, ChoiceBox<String> choiceBoxCategory) {
        this.treeView = inputTreeView;
        this.choiceBoxCategory = choiceBoxCategory;
    }
    public void start() {
        // Create the root TreeItem
        TreeItem<Category> rootItem = new TreeItem<>(new Category(-2,-2,"","--Chọn--"));
        rootItem.setExpanded(true);

        // Get the categories from the database
        List<Category> categories = getCategoriesFromDatabase();

        // Build the category tree
        buildCategoryTree(rootItem, categories, -1);


        treeView.setRoot(rootItem);
        // Expand all tree nodes
        expandAll(treeView);

        // Set the event handler for mouse click
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Category categorySelected = newValue.getValue();
                choiceBoxCategory.setValue(categorySelected.getName());
                treeView.setVisible(false);
                idTreeViewChoice = categorySelected.getId();
            }
        });
    }
    public int getIdChoice () {
        return idTreeViewChoice;
    }
    private List<Category> getCategoriesFromDatabase() {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Category")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int parentId = resultSet.getInt("parent_id");
                String name = resultSet.getString("name");
                String info = resultSet.getString("info");

                Category category = new Category(id, parentId, info, name);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    private void buildCategoryTree(TreeItem<Category> parentItem, List<Category> categories, int parentId) {
        for (Category category : categories) {
            if (category.getParentId() == parentId) {
                TreeItem<Category> categoryItem = new TreeItem<>(category);
                parentItem.getChildren().add(categoryItem);
                buildCategoryTree(categoryItem, categories, category.getId());
            }
        }
    }

    private void expandAll(TreeView<?> treeView) {
        treeView.getRoot().setExpanded(true);
        treeView.getRoot().getChildren().forEach(item -> expandTreeItem(item, true));
    }

    private void expandTreeItem(TreeItem<?> item, boolean expand) {
        item.setExpanded(expand);
        item.getChildren().forEach(child -> expandTreeItem(child, expand));
    }

}

