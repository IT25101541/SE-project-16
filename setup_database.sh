#!/bin/bash
# ====================================================================
# CreativePulse - Automated MySQL Setup & Connection Script
# ====================================================================

MYSQL_BIN="/usr/local/mysql/bin/mysql"
if [ ! -f "$MYSQL_BIN" ]; then
    MYSQL_BIN=$(which mysql)
fi

DB_USER="root"
DB_PASS="12345678"
SQL_FILE="$(dirname "$0")/src/main/resources/schema_and_seed.sql"

echo "============================================================"
echo "  CREATIVEPULSE - DATABASE INITIALIZATION & VERIFICATION"
echo "============================================================"

# Check if MySQL port 3306 is open
if ! lsof -i :3306 > /dev/null 2>&1; then
    echo "⚠️  MySQL is not currently running on port 3306."
    echo "Starting MySQL server..."
    sudo /usr/local/mysql/support-files/mysql.server start
fi

# Run schema script
echo "Importing schema and seed data from schema_and_seed.sql..."
"$MYSQL_BIN" -u "$DB_USER" -p"$DB_PASS" < "$SQL_FILE"

if [ $? -eq 0 ]; then
    echo "✅ Database 'creativepulse_db' initialized successfully!"
    echo ""
    echo "Summary of created tables and sample records:"
    "$MYSQL_BIN" -u "$DB_USER" -p"$DB_PASS" -e "
        USE creativepulse_db;
        SELECT 'Users' AS TableName, COUNT(*) AS RecordCount FROM users
        UNION ALL
        SELECT 'Employees', COUNT(*) FROM employees
        UNION ALL
        SELECT 'Clients', COUNT(*) FROM clients
        UNION ALL
        SELECT 'Campaigns (Your Module)', COUNT(*) FROM campaigns
        UNION ALL
        SELECT 'Ad Designs', COUNT(*) FROM advertisement_designs
        UNION ALL
        SELECT 'Tasks', COUNT(*) FROM tasks
        UNION ALL
        SELECT 'Invoices', COUNT(*) FROM invoices
        UNION ALL
        SELECT 'Payments', COUNT(*) FROM payments
        UNION ALL
        SELECT 'Saved Reports', COUNT(*) FROM saved_reports;
    "
    echo ""
    echo "============================================================"
    echo "  All 6 group modules are now connected in the database!"
    echo "============================================================"
else
    echo "❌ Failed to connect to MySQL. Please verify your root password."
fi
