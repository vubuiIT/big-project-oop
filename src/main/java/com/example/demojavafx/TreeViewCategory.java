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
    private final TreeView<String> treeView;
    private final ChoiceBox<String> choiceBoxCategory;

    public int idTreeViewChoice = -1;
    public TreeViewCategory(TreeView <String> inputTreeView, ChoiceBox<String> choiceBoxCategory) {
        this.treeView = inputTreeView;
        this.choiceBoxCategory = choiceBoxCategory;
    }
    public void start() {
        // Create the root TreeItem
        TreeItem<String> rootItem = new TreeItem<>("_Categories_");
        rootItem.setExpanded(true);

        // Get the categories from the database
        List<Category> categories = getCategoriesFromDatabase();

        // Build the category tree
        buildCategoryTree(rootItem, categories, -1);


        treeView.setRoot(rootItem);
        // Expand all tree nodes
        expandAll(treeView);

        // Set the event handler for mouse click
        treeView.setOnMouseClicked(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String selectedCategoryName = selectedItem.getValue();
                if (!Objects.equals(selectedCategoryName, "_Categories_")) {
                    choiceBoxCategory.setValue(selectedCategoryName);
                    int selectedCategoryId = getCategoryByName(categories, selectedCategoryName).getId();
                    treeView.setVisible(false);
                    idTreeViewChoice = selectedCategoryId;
                }

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

                Category category = new Category(id, parentId, name, info);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    private void buildCategoryTree(TreeItem<String> parentItem, List<Category> categories, int parentId) {
        for (Category category : categories) {
            if (category.getParentId() == parentId) {
                TreeItem<String> categoryItem = new TreeItem<>(category.getName());
                parentItem.getChildren().add(categoryItem);
                buildCategoryTree(categoryItem, categories, category.getId());
            }
        }
    }

    private Category getCategoryByName(List<Category> categories, String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;
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

