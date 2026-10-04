#!/bin/bash
# CreativePulse - MySQL setup. Runs every db/*.sql file in order.
MYSQL_BIN="/usr/local/mysql/bin/mysql"
[ -f "$MYSQL_BIN" ] || MYSQL_BIN=$(which mysql)

DB_USER="${DB_USER:-root}"
DB_PASS="${DB_PASS:-12345678}"
DB_DIR="$(cd "$(dirname "$0")" && pwd)/db"

if ! lsof -i :3306 > /dev/null 2>&1; then
    echo "MySQL is not running on port 3306. Starting it..."
    sudo /usr/local/mysql/support-files/mysql.server start
fi

for f in "$DB_DIR"/*.sql; do
    echo "Running $(basename "$f") ..."
    "$MYSQL_BIN" -u "$DB_USER" -p"$DB_PASS" < "$f" || { echo "Failed on $f"; exit 1; }
done

echo "Tables in creativepulse_db:"
"$MYSQL_BIN" -u "$DB_USER" -p"$DB_PASS" -e "USE creativepulse_db; SHOW TABLES;"
