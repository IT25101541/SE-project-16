#!/bin/bash
# ====================================================================
# CreativePulse - MySQL Startup Fix Script for macOS
# Resolves: "ERROR! The server quit without updating PID file"
# ====================================================================

if [ "$EUID" -ne 0 ]; then
  echo "⚠️  Please run this script with sudo: sudo ./fix_mysql.sh"
  exit 1
fi

echo "============================================================"
echo "  Fixing MySQL Server Startup on macOS..."
echo "============================================================"

# Step 1: Kill any hanging/zombie mysqld processes holding file locks
echo "[1/4] Terminating any zombie MySQL processes..."
killall -9 mysqld 2>/dev/null || pkill -9 mysqld 2>/dev/null || true
sleep 1

# Step 2: Remove stale PID and socket files
echo "[2/4] Removing stale PID, lock, and socket files..."
rm -f /usr/local/mysql/data/*.pid 2>/dev/null || true
rm -f /usr/local/mysql/data/*.pid.shutdown 2>/dev/null || true
rm -f /tmp/mysql.sock 2>/dev/null || true
rm -f /tmp/mysql.sock.lock 2>/dev/null || true
rm -f /tmp/mysqlx.sock 2>/dev/null || true
rm -f /tmp/mysqlx.sock.lock 2>/dev/null || true

# Step 3: Restore proper ownership and permissions to _mysql user
echo "[3/4] Resetting data directory ownership to _mysql:_mysql..."
chown -R _mysql:_mysql /usr/local/mysql/data
chmod -R 750 /usr/local/mysql/data

# Step 4: Start MySQL Server
echo "[4/4] Starting MySQL Server..."
/usr/local/mysql/support-files/mysql.server start

if [ $? -eq 0 ]; then
  echo ""
  echo "============================================================"
  echo "🎉 SUCCESS! MySQL is now running on port 3306."
  echo "============================================================"
  echo "Now initializing CreativePulse database..."
  /usr/local/mysql/bin/mysql -u root -p12345678 < "$(dirname "$0")/src/main/resources/schema_and_seed.sql"
  if [ $? -eq 0 ]; then
    echo "✅ creativepulse_db initialized and seeded successfully!"
  fi
else
  echo ""
  echo "❌ MySQL still failed to start. Last 20 lines of the error log:"
  echo "------------------------------------------------------------"
  tail -n 20 /usr/local/mysql/data/*.err
  echo "------------------------------------------------------------"
fi
