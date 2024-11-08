# TinyDB - CSCI 5408 (S24)

## Project Overview

TinyDB is a basic database management system (DBMS) created as part of the CSCI 5408 course at Dalhousie University. The project allows users to perform fundamental DBMS operations through a command-line interface and provides hands-on experience in core database functionalities, including data storage, query execution, transaction processing, and logging, without relying on third-party libraries.

---

## Objective

The objective of TinyDB is to build and understand essential components of a database system, such as:
- Data storage
- Query execution
- Logging
- Transaction processing

---

## Features and Modules

TinyDB comprises seven primary modules, each designed to offer a specific DBMS functionality:

### 1. DB Design
- Implements linear data structures for query and data processing.
- Stores data persistently in a custom file format (excluding JSON/XML/CSV/binary).
- Maintains a data dictionary and metadata storage in the same custom format.

### 2. Query Implementation
Supports basic SQL-like commands, including:
- `CREATE DATABASE`
- `USE DATABASE`
- `CREATE TABLE`
- `INSERT`
- `SELECT` 
- `UPDATE` 
- `DELETE`
- `DROP TABLE`

### 3. Transaction Processing
- Identifies and processes transactions separately from regular queries.
- Implements ACID properties to ensure robust transaction handling.

### 4. Log Management
Maintains three types of logs, stored in JSON format:
- **General Logs:** Records query execution time and database state.
- **Event Logs:** Tracks database changes, concurrent transactions, and crash reports.
- **Query Logs:** Stores user queries with timestamps.

### 5. Data Modeling (Reverse Engineering)
- Generates a text-based Entity-Relationship Diagram (ERD) with details on tables, columns, relationships, and cardinality.

### 6. Export Structure & Values
- Allows exporting of the database structure and data as a standard SQL dump.

### 7. User Interface & Login Security
- CLI-based, menu-driven user interface.
- Basic login functionality with hashed user IDs and passwords.
- Registration includes storing a user ID, password, and security questions.
- Options available for registered users:
  - Write Queries
  - Export Data and Structure
  - Generate ERD
  - Exit

---

## Usage

### Running the Application
Launch the application from the command line and follow the menu-driven interface for various DBMS operations.
