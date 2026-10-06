# Hosting CreativePulse for free

Two files were added to make this possible — `Dockerfile` and `.dockerignore`
— plus three lines in `application.properties` were changed so the database
connection can be overridden by environment variables. Nothing about how the
app behaves changed; locally, with no environment variables set, it runs
exactly as it did before.

The free stack:

| Piece | Service | Why |
|---|---|---|
| The app | [Render](https://render.com) | Free web service, deploys straight from a Dockerfile, no card needed |
| The database | [db4free.net](https://www.db4free.net) | Free MySQL, no card needed, made for exactly this (testing/education) |

**Two honest limitations of the free tier, so nothing surprises you:**
- Render's free web service spins down after 15 minutes with no visitors. The
  next visit takes about a minute to wake back up. Open the link yourself a
  minute before showing anyone else.
- db4free.net is a *testing* service — it's not guaranteed uptime and isn't
  meant for anything that matters long-term. Perfect for a demo, not for a
  real client.
- Uploaded advertisement images are written to local disk (`uploads/`). Render's
  free tier doesn't persist disk writes across restarts, so uploaded files
  disappear whenever the service spins down and back up. Everything else
  (users, clients, campaigns, tasks, invoices — all stored in MySQL) is fine.

---

## 1. Create the free database

1. Go to **https://www.db4free.net/signup.php**
2. Pick a database name, username and password. **Write these down** — you'll
   need them in step 3. The database name you choose is created immediately;
   you don't run any CREATE DATABASE statement yourself.
3. Check your email and click the confirmation link.
4. Your connection details are:
   - Host: `db4free.net`
   - Port: `3306`
   - Database name: whatever you chose in step 2

## 2. Push the project to GitHub

Render deploys from a Git repository.

```bash
cd creativepulse
git init
git add .
git commit -m "Initial commit"
```

Create an empty repository on GitHub (no README, no .gitignore — just the
bare repo), then:

```bash
git remote add origin https://github.com/YOUR_USERNAME/creativepulse.git
git branch -M main
git push -u origin main
```

## 3. Create the web service on Render

1. Sign up at **https://render.com** (GitHub login is fastest).
2. **New +** → **Web Service** → connect the `creativepulse` repository.
3. Render will detect the `Dockerfile` automatically. If asked:
   - **Runtime:** Docker
   - **Instance type:** Free
4. Before clicking **Create Web Service**, add these environment variables
   (Render calls this section "Environment"):

   | Key | Value |
   |---|---|
   | `DATABASE_URL` | `jdbc:mysql://db4free.net:3306/YOUR_DB_NAME?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` |
   | `DATABASE_USERNAME` | the username you picked on db4free.net |
   | `DATABASE_PASSWORD` | the password you picked on db4free.net |

   Replace `YOUR_DB_NAME` with the exact database name from step 1.

5. Click **Create Web Service**. Render builds the Docker image and deploys —
   the first build takes a few minutes since it downloads Maven dependencies.

## 4. Test it

Render gives you a URL like `https://creativepulse.onrender.com`. Open it —
the first request will be slow (cold start), then the login page will
appear. Sign in with any of the seeded demo accounts (`admin` / `1234`, etc.)
exactly as you do locally. Hibernate creates all the tables automatically on
first startup (`spring.jpa.hibernate.ddl-auto=update`), and `DataSeeder`
populates the demo accounts the same way it does locally.

## Rolling back to local-only

If you ever want to stop deploying and just run this locally again, nothing
needs to change — don't set the three environment variables, and the app
falls back to `localhost:3306` with your local MySQL exactly as before
these changes.
