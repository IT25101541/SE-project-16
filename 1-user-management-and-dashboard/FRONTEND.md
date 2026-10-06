# CreativePulse — front end

The interface was rebuilt around a light "studio" theme: white surfaces, violet
brand ink, mint for healthy signals and pink for anything that needs attention.

## Where the front end lives

| File | Purpose |
|---|---|
| `src/main/resources/static/css/app.css` | The entire theme. Design tokens sit in `:root`, every component below it. |
| `src/main/resources/static/js/app.js` | Active navigation, mobile drawer, count-up figures, bar chart scaling, status pill colouring, credential chips. |
| `src/main/resources/templates/layout.html` | Sidebar + topbar shared by every page. |
| `src/main/resources/templates/login.html` | Split-screen sign-in with one-tap demo accounts. |
| `src/main/resources/templates/dashboard.html` | Home page. |

Every other template (`clients/`, `campaigns/`, `tasks/`, `users/`,
`invoices/`, `reports/`, `advertisements/`) inherits the theme through
`layout.html` and `app.css` — no per-page styling.

## Design tokens

Change a value in `:root` and it propagates everywhere.

```css
--violet:  #5b4bff;   /* brand */
--mint:    #17c07f;   /* healthy / paid / approved */
--rose:    #ff4d8d;   /* unpaid / overdue / rejected */
--amber:   #f5a524;   /* pending / in progress */
--canvas:  #f5f6fb;   /* page background */
--surface: #ffffff;   /* cards */
```

## Navigation

The sidebar menu is gated with `sec:authorize`, matching `SecurityConfig`
exactly, so each role only sees the modules it owns:

| Role | Modules |
|---|---|
| ADMIN | all seven |
| SALES | Clients |
| MANAGER | Campaigns, Tasks, Advertisements, Reports |
| DESIGNER | Tasks, Advertisements |
| FINANCE | Billing & payments |
| CLIENT | Home only |

The current module highlights itself. `app.js` compares `window.location.pathname`
against each link and marks the longest match, so no template needs to declare
which page it is. Below 992px the sidebar becomes a drawer (burger button,
backdrop, Escape to close).

## Demo accounts

The login page shows six tappable chips. Tapping one fills the form.
All passwords are `1234`, seeded by `DataSeeder`.

`admin` · `sales01` · `manager01` · `designer01` · `finance01` · `client01`

## Dashboard

Every figure comes from the model attributes `AuthController` already supplies —
nothing is hard-coded. Bar widths are scaled against the largest value in the
group by `app.js`, so the chart stays readable whatever the data looks like.

## Notes

* Bootstrap 5.3.3, Bootstrap Icons 1.11.3 and Plus Jakarta Sans load from CDN,
  so the machine needs internet on first load. Download them into
  `static/vendor/` and swap the `<link>` tags if you need to demo offline.
* Status text is presented in sentence case in the browser (`IN_PROGRESS`
  renders as "In progress"). The database values are untouched.
* Reduced motion is respected, focus rings are visible, and the print
  stylesheet hides the sidebar and topbar so invoices and reports print clean.
