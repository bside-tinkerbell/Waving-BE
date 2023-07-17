const host = "118.67.135.57";
const port = 3306;
const database = "greeting";
const username = "tinkerbell";
const password = "tinkerbell";

let sheetName = "인사한마디DB_액기스"

/**
 * Export Spreadsheet to MySQL.
 */

function exportGreetingToMySQL() {
    Logger.log(SpreadsheetApp.getActiveSpreadsheet().getUrl());
    let sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(sheetName);
    Logger.log(sheet);
    let data = sheet.getDataRange().getValues();

    // Connect to the MySQL database
    let connection = Jdbc.getConnection('jdbc:mysql://' + host + ':' + port + '/' + database, username, password);

    // Prepare the insert statement
    // var insertGreetingCategoryStmt = connection.prepareStatement('INSERT INTO greeting_category (category) VALUES (?) ON DUPLICATE KEY UPDATE category = category');
    let insertGreetingCategoryStmt = connection.prepareStatement('INSERT IGNORE greeting_category (greeting_category_id, category) VALUES (?, ?)');
    let insertGreetingStmt = connection.prepareStatement('INSERT IGNORE greeting (greeting_category_id, greeting) VALUES (?, ?)');

    const greetingSet = new Set();
    const greetingCategoryMap = new Map();
    let greetingCategoryIdx = 1;
    // Start from row 2 to skip the header
    for (let i = 1; i < data.length; i++) {
        let row = data[i];
        // insertStmt.setString(1, row['NO']);  // Replace with your column names
        // insertStmt.setString(2, row['인사말']);
        // Logger.log(row.toString())
        // Logger.log(row[2]);

        let greeting = row[1];
        let greetingCategory = row[2];

        if (!greetingCategoryMap.has(greetingCategory)) {
            greetingCategoryMap.set(greetingCategory, greetingCategoryIdx);
            insertGreetingCategoryStmt.setString(1, greetingCategoryIdx);
            insertGreetingCategoryStmt.setString(2, greetingCategory);
            insertGreetingCategoryStmt.addBatch();
            greetingCategoryIdx++;
        }

        if (!greetingSet.has(greeting)) {
            greetingSet.add(greeting);
            insertGreetingStmt.setString(1, greetingCategoryMap.get(greetingCategory));
            insertGreetingStmt.setString(2, greeting);
            insertGreetingStmt.addBatch();
        }
    }

    // Execute the batch insert
    insertGreetingCategoryStmt.executeBatch();
    insertGreetingStmt.executeBatch();

    // Close the connections
    insertGreetingCategoryStmt.close();
    insertGreetingStmt.close();
    // stmt.close();
    connection.close();

    Logger.log('Data exported to MySQL successfully.');
}