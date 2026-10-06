/* ============================================================
   CreativePulse — shared front-end behaviour
   No build step, no framework. Plain ES5-friendly JavaScript.
   ============================================================ */
(function () {
  "use strict";

  var reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  /* ---------------------------------------------------------
     1. Sidebar: highlight the module the user is currently in.
        Done in JS so it keeps working on every Thymeleaf page
        without repeating an "activePage" variable in each one.
     --------------------------------------------------------- */
  function markActiveNav() {
    var here = window.location.pathname.replace(/\/+$/, "") || "/";
    var links = document.querySelectorAll(".sidebar a.nav-item[href]");
    var best = null, bestLen = -1;

    for (var i = 0; i < links.length; i++) {
      var path = links[i].pathname.replace(/\/+$/, "") || "/";
      if (path === "/") continue;
      if (here === path || here.indexOf(path + "/") === 0) {
        if (path.length > bestLen) { best = links[i]; bestLen = path.length; }
      }
    }
    // "/" lands on the dashboard
    if (!best && (here === "/" || here === "")) {
      best = document.querySelector('.sidebar a.nav-item[data-home="true"]');
    }
    if (best) best.classList.add("active");
  }

  /* ---------------------------------------------------------
     2. Mobile drawer
     --------------------------------------------------------- */
  function initDrawer() {
    var sidebar  = document.querySelector(".sidebar");
    var backdrop = document.querySelector(".sidebar-backdrop");
    var burger   = document.querySelector(".burger");
    if (!sidebar || !burger) return;

    function close() {
      sidebar.classList.remove("open");
      if (backdrop) backdrop.classList.remove("show");
    }
    burger.addEventListener("click", function () {
      sidebar.classList.toggle("open");
      if (backdrop) backdrop.classList.toggle("show");
    });
    if (backdrop) backdrop.addEventListener("click", close);
    document.addEventListener("keydown", function (e) {
      if (e.key === "Escape") close();
    });
  }

  /* ---------------------------------------------------------
     3. Count-up for the KPI numbers on the dashboard
     --------------------------------------------------------- */
  function countUp() {
    var nodes = document.querySelectorAll("[data-count]");
    for (var i = 0; i < nodes.length; i++) {
      (function (el) {
        var target = parseInt(el.getAttribute("data-count"), 10) || 0;
        if (reduceMotion || target === 0) { el.textContent = target; return; }
        var start = null, dur = 750;
        function step(ts) {
          if (!start) start = ts;
          var p = Math.min((ts - start) / dur, 1);
          var eased = 1 - Math.pow(1 - p, 3);
          el.textContent = Math.round(target * eased);
          if (p < 1) requestAnimationFrame(step);
        }
        requestAnimationFrame(step);
      })(nodes[i]);
    }
  }

  /* ---------------------------------------------------------
     4. Measure bars — width is scaled against the largest value
        in the same group, so the chart always fills nicely.
     --------------------------------------------------------- */
  function drawBars() {
    var groups = document.querySelectorAll("[data-bars]");
    for (var g = 0; g < groups.length; g++) {
      var fills = groups[g].querySelectorAll(".m-fill[data-value]");
      var max = 0, k;
      for (k = 0; k < fills.length; k++) {
        max = Math.max(max, parseFloat(fills[k].getAttribute("data-value")) || 0);
      }
      if (max <= 0) max = 1;
      for (k = 0; k < fills.length; k++) {
        var v = parseFloat(fills[k].getAttribute("data-value")) || 0;
        var pct = Math.max(6, Math.round((v / max) * 100));   // 6% floor so a zero bar is still visible
        fills[k].style.width = reduceMotion ? pct + "%" : "0%";
        if (!reduceMotion) {
          (function (el, p) { setTimeout(function () { el.style.width = p + "%"; }, 120); })(fills[k], pct);
        }
      }
    }
  }

  /* ---------------------------------------------------------
     5. Tidy the role text Spring Security prints
        ("[ROLE_ADMIN]" -> "Administrator")
     --------------------------------------------------------- */
  var ROLE_LABEL = {
    ADMIN: "System Administrator",
    SALES: "Sales Executive",
    MANAGER: "Campaign Manager",
    DESIGNER: "Graphic Designer",
    FINANCE: "Finance Officer",
    CLIENT: "Client"
  };
  function prettyRoles() {
    var nodes = document.querySelectorAll("[data-role-text]");
    for (var i = 0; i < nodes.length; i++) {
      var raw = nodes[i].textContent || "";
      var m = raw.match(/ROLE_([A-Z]+)/);
      var key = m ? m[1] : "";
      nodes[i].textContent = ROLE_LABEL[key] || (key ? key : raw.trim());
    }
  }

  /* ---------------------------------------------------------
     5b. Status pills: colour them by meaning and turn
         DB values like IN_PROGRESS into "In progress".
         Runs on every module list, so the templates stay clean.
     --------------------------------------------------------- */
  var STATUS_TONE = {
    /* good */
    ACTIVE: "ok", APPROVED: "ok", PAID: "ok", DONE: "ok",
    /* in flight */
    PLANNED: "warn", PAUSED: "warn", SUBMITTED: "warn", PENDING: "warn",
    IN_PROGRESS: "warn", REVIEW: "warn", PARTIALLY_PAID: "warn", MEDIUM: "warn",
    /* attention */
    CANCELLED: "bad", REJECTED: "bad", UNPAID: "bad", OVERDUE: "bad", HIGH: "bad",
    /* neutral */
    DRAFT: "mute", TODO: "mute", LOW: "mute", INACTIVE: "mute",
    /* informational */
    COMPLETED: "info"
  };
  var TONE_CLASS = {
    ok:   ["bg-success-subtle", "text-success-emphasis"],
    warn: ["bg-warning-subtle", "text-warning-emphasis"],
    bad:  ["bg-danger-subtle", "text-danger-emphasis"],
    mute: ["bg-secondary-subtle", "text-secondary-emphasis"],
    info: ["bg-primary-subtle", "text-primary-emphasis"]
  };
  function paintStatusBadges() {
    var pills = document.querySelectorAll(".badge-soft");
    for (var i = 0; i < pills.length; i++) {
      var el = pills[i];
      var raw = (el.textContent || "").trim();
      if (!/^[A-Z][A-Z_]*$/.test(raw)) continue;          // leave normal words alone
      var tone = STATUS_TONE[raw];
      if (tone) {
        el.className = el.className
          .replace(/\bbg-[a-z]+-subtle\b/g, "")
          .replace(/\btext-[a-z]+-emphasis\b/g, "")
          .trim();
        el.classList.add(TONE_CLASS[tone][0], TONE_CLASS[tone][1]);
      }
      var words = raw.toLowerCase().split("_");
      el.textContent = words[0].charAt(0).toUpperCase() + words[0].slice(1) +
                       (words.length > 1 ? " " + words.slice(1).join(" ") : "");
    }
  }

  /* ---------------------------------------------------------
     6. Auto-dismiss flash messages
     --------------------------------------------------------- */
  function autoDismiss() {
    var alerts = document.querySelectorAll(".auto-dismiss");
    for (var i = 0; i < alerts.length; i++) {
      (function (el) {
        setTimeout(function () {
          el.style.transition = "opacity .4s ease";
          el.style.opacity = "0";
          setTimeout(function () { el.remove(); }, 420);
        }, 5000);
      })(alerts[i]);
    }
  }

  /* ---------------------------------------------------------
     7. Login page: demo credential chips fill the form
     --------------------------------------------------------- */
  function initCredChips() {
    var chips = document.querySelectorAll(".cred-chip");
    if (!chips.length) return;
    var u = document.getElementById("username");
    var p = document.getElementById("password");
    for (var i = 0; i < chips.length; i++) {
      chips[i].addEventListener("click", function () {
        if (u) u.value = this.getAttribute("data-user");
        if (p) p.value = this.getAttribute("data-pass");
        if (p) p.focus();
      });
    }
  }

  /* ---------------------------------------------------------
     8. Show / hide password
     --------------------------------------------------------- */
  function initPasswordToggle() {
    var btn = document.getElementById("togglePw");
    var pw = document.getElementById("password");
    if (!btn || !pw) return;
    btn.addEventListener("click", function () {
      var hidden = pw.type === "password";
      pw.type = hidden ? "text" : "password";
      btn.innerHTML = '<i class="bi ' + (hidden ? "bi-eye-slash" : "bi-eye") + '"></i>';
      btn.setAttribute("aria-label", hidden ? "Hide password" : "Show password");
    });
  }

  /* ---------------------------------------------------------
     9. Login page: "Continue with Google" — demo has no SSO
        wired up yet, so show a small honest toast instead of
        a dead click or a fake redirect.
     --------------------------------------------------------- */
  function initGoogleStub() {
    var btn = document.getElementById("googleBtn");
    var host = document.getElementById("toastHost");
    if (!btn || !host) return;
    btn.addEventListener("click", function () {
      var pill = document.createElement("div");
      pill.className = "toast-pill";
      pill.innerHTML = '<i class="bi bi-info-circle"></i> Google sign-in isn\'t connected in this demo — please use a username and password above.';
      host.appendChild(pill);
      requestAnimationFrame(function () { pill.classList.add("show"); });
      setTimeout(function () {
        pill.classList.remove("show");
        setTimeout(function () { pill.remove(); }, 250);
      }, 4200);
    });
  }

  document.addEventListener("DOMContentLoaded", function () {
    markActiveNav();
    initDrawer();
    countUp();
    drawBars();
    prettyRoles();
    paintStatusBadges();
    autoDismiss();
    initCredChips();
    initPasswordToggle();
    initGoogleStub();
  });
})();
