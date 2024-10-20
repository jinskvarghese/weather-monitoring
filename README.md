# Weather Monitoring and Forecast Application

This project is a weather monitoring and forecasting web application built using Spring Boot, Thymeleaf, and Chart.js. It fetches weather data from the OpenWeatherMap API, processes it to provide real-time monitoring, daily summaries, and future weather forecasts, and displays visualizations using interactive charts.

## Features
1.Weather Data Monitoring:

- Real-time data collection every 5 minutes for multiple cities.
- Displays temperature, humidity, wind speed, and weather conditions.
2. Daily Weather Summaries:

- Calculates daily averages, maximum, and minimum values.
- Identifies the dominant weather condition for the day.
3. Real-Time Alerts:

- Generates alerts for extreme weather conditions (e.g., high temperatures).
4. Weather Forecast Retrieval:

- Provides 3-hour interval forecasts for the next few days.
- Forecast data includes temperature, feels-like temperature, humidity, wind speed, and weather conditions.
5. Data Visualization:

- Line chart for temperature trends.
- Pie chart for humidity distribution.
- Bar chart for wind speed variations.
6. Additional Features:

- Forecast summaries based on predicted weather conditions.
- Frontend integration of alerts with dynamic message display.

## Dependencies

### Docker / Podman
- Docker or Podman: Required for running containers for the database and web server.

### Software
- Java 17 or later: Required for running the Spring Boot application.
- Maven: For building the application.
- Git: To clone the project repository.

### Libraries and Tools
- Spring Boot (2.7+): Backend framework for Java.
- Thymeleaf: Templating engine for rendering HTML.
- H2 Database: In-memory database for storing weather data and alerts.
- OpenWeatherMap API: For fetching real-time weather data.
- Chart.js: JavaScript library for creating charts in the frontend.

### Environment Setup
Make sure the following are installed:
- Docker / Podman
- Java 17+
- Maven

## Getting Started

### Step 1: Clone the Repository
Clone the repository to your local machine using:
```bash
git clone https://github.com/your-username/weather-monitoring-app.git
cd weather-monitoring-app
```

### Step 2: Setup the Environment
Set up the required environment variables:

Create a .env file in the project's root directory with the following contents:
makefile
Copy code
OPENWEATHERMAP_API_KEY=your_openweathermap_api_key
DATA_REFRESH_INTERVAL=300000 # Data refresh interval in milliseconds (5 minutes)
Make sure to replace your_openweathermap_api_key with your actual OpenWeatherMap API key.
Step 3: Configure Database
The application uses an H2 in-memory database. No additional configuration is needed. However, if you wish to use a different database like MySQL or PostgreSQL, update the application.properties file accordingly.

### Step 4: Run Docker Containers
Run the containers required for the application:

# Pull the H2 image
docker pull oscarfonts/h2

# Run the H2 database
docker run -d --name h2-database -p 8082:8082 oscarfonts/h2

# Access the H2 Console at http://localhost:8082 with the JDBC URL: `jdbc:h2:mem:testdb`
Step 5: Build and Run the Application
Build the application using Maven:

`mvn clean install`
Run the Spring Boot application:

`mvn spring-boot:run`
The application will start at http://localhost:8080.

## Design Choices
1. Technology Stack
**Spring Boot**: Chosen for its ease of integration with RESTful APIs, database support, and simplicity in creating scheduled tasks.
**Thymeleaf**: Used for server-side rendering of dynamic HTML content, which provides a simpler way to integrate with the backend data.
**Chart.js**: A straightforward and flexible library for visualizing data in charts.
**H2 Database**: An in-memory database is used for simplicity and quick access. Can be replaced with a persistent DB for production.

2. Data Flow and Architecture
- The WeatherService is responsible for fetching weather data from the OpenWeatherMap API, processing it, and storing it in the database.
- A scheduled task fetches weather data every 5 minutes and processes it for daily summaries.
- Another scheduled task triggers alerts based on defined conditions (e.g., high temperature).
- The controller layer fetches data from the database and populates Thymeleaf templates for the frontend.
- Charts in the frontend are dynamically rendered using Thymeleaf for data integration and Chart.js for visual representation.

3. Alerting Mechanism
Alerts are generated when certain thresholds are exceeded. For instance, if the temperature in a city exceeds a certain limit consecutively for a defined period, an alert is triggered and stored in the database.

## API Configuration
**OpenWeatherMap API Integration**
The application integrates with the OpenWeatherMap API to fetch real-time weather data. The API key should be configured in the .env file, and the URL should be set in the application.properties file:
```shell
openweathermap.api.url=https://api.openweathermap.org/data/2.5/weather
openweathermap.api.key=${OPENWEATHERMAP_API_KEY}
data.refresh.interval=${DATA_REFRESH_INTERVAL}
```
## API Endpoints 
- /home: Home page
- /daily-summaries: Displays daily weather summaries.
- /weather-summary: Real-time weather monitoring.
- /forecast: Displays the weather forecast.
- /alerts: Shows all triggered alerts.

## Additional Features Added
- Integration of new weather parameters (humidity, wind speed).
- Visualization charts for the additional parameters.
- Forecast data retrieval and summarization.
- Dynamic alert generation for forecasts.

## Running Tests
To run unit tests, use:
`mvn test`

## Future Enhancements 
- Implement persistent storage using MySQL or PostgreSQL.
- Use Docker Compose for automated setup of containers.
- Add support for more weather parameters and multiple data sources.
- Implement user authentication and role-based access control.