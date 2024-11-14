# Bulletin Board Application

This is a web-based Bulletin Board application implemented using Spring Boot. The application allows users to create, view, modify, and delete posts on a bulletin board.

## Features

1. **List of Posts**
    - View a list of all created posts
    - Each post in the list displays:
        - Post number (auto-incrementing integer)
        - Post title (up to 50 characters in Korean, 100 in English)
        - Author name (up to 10 characters)
        - Number of views
        - Creation date
    - Clicking on a post title navigates to the post details
    - View count increments each time a post is viewed

2. **Post Details**
    - Displays full post information:
        - Post number
        - Title
        - Author name
        - View count
        - Creation date
        - Full content
    - Includes buttons for post modification and deletion
    - Shows a button to return to the post list
    - For modified posts, displays the modification time

3. **Create a Post**
    - Allows users to create new posts
    - Collects the following information:
        - Author name
        - Password
        - Post content
    - Saves the post to the database with creation timestamp

4. **Modify a Post**
    - Allows modification of existing posts
    - Requires the original post password for authentication
    - Displays the same fields as the Create Post page
    - Pre-fills the form with existing post data
    - Updates the database and records the modification time

5. **Delete a Post**
    - Allows deletion of posts
    - Requires the original post password for authentication
    - Soft deletes posts (keeps them in the database but hides from the list)

## Technical Details

- Developed using Spring Boot framework
- Uses MyBatis for database operations
- Frontend utilizes jQuery and Bootstrap
- Database: PostgreSQL (RDBMS)
- Includes Swagger UI for API documentation

## Getting Started

1. Clone the repository
2. Ensure you have Java and Maven installed
3. Set up a PostgreSQL database
4. Update `application.properties` with your database credentials
5. Run the application using `mvn spring-boot:run`
6. Access the application at `http://localhost:8080`
7. Access Swagger UI at `http://localhost:8080/swagger-ui.html`

## Database Setup

The application automatically sets up the database schema and inserts sample data on first run. The SQL scripts for this are located in:

- `src/main/resources/schema.sql`: Database schema
- `src/main/resources/data.sql`: Sample data

## Development Notes

- The backend server is developed first, followed by the frontend
- Input validation is implemented to ensure data integrity
- Deleted posts are soft-deleted (not removed from the database)