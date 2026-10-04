#!/bin/bash
# CreativePulse - MySQL startup fix for macOS
# Resolves: "ERROR! The server quit without updating PID file"

if [ "$EUID" -ne 0 ]; then
  echo "Please run with sudo: sudo ./fix_mysql.sh"
  exit 1
fi

echo "[1/4] Terminating zombie MySQL processes..."
killall -9 mysqld 2>/dev/null || pkill -9 mysqld 2>/dev/null || true
sleep 1

echo "[2/4] Removing stale PID, lock and socket files..."
rm -f /usr/local/mysql/data/*.pid /usr/local/mysql/data/*.pid.shutdown 2>/dev/null || true
rm -f /tmp/mysql.sock /tmp/mysql.sock.lock /tmp/mysqlx.sock /tmp/mysqlx.sock.lock 2>/dev/null || true

echo "[3/4] Resetting data directory ownership..."
chown -R _mysql:_mysql /usr/local/mysql/data
chmod -R 750 /usr/local/mysql/data

echo "[4/4] Starting MySQL..."
if /usr/local/mysql/support-files/mysql.server start; then
  echo "MySQL is running on port 3306. Initialising database..."
  bash "$(dirname "$0")/setup_database.sh"
else
  echo "MySQL still failed to start. Last 20 lines of the error log:"
  tail -n 20 /usr/local/mysql/data/*.err
fi
