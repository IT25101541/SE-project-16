/* CreativePulse — Premium interaction layer.
   Pure progressive enhancement: no backend/data dependency,
   safe to load on every page. */
(function () {
  "use strict";

  document.addEventListener("DOMContentLoaded", function () {
    animateCounters();
    animateProgressBars();
    staggerTableRows();
    wireButtonRipples();
    autoDismissAlert();
  });

  /* Animate .stat strong numbers counting up from 0 */
  function animateCounters() {
    var nodes = document.querySelectorAll(".stat strong");
    nodes.forEach(function (el) {
      var raw = (el.textContent || "").trim();
      var match = raw.match(/\d+/);
      if (!match) return;
      var target = parseInt(match[0], 10);
      var suffix = raw.replace(match[0], "");
      var duration = 900;
      var start = null;

      function step(ts) {
        if (start === null) start = ts;
        var progress = Math.min((ts - start) / duration, 1);
        var eased = 1 - Math.pow(1 - progress, 3);
        el.textContent = Math.round(eased * target) + suffix;
        if (progress < 1) requestAnimationFrame(step);
      }
      el.textContent = "0" + suffix;
      requestAnimationFrame(step);
    });
  }

  /* Progress bars are set via inline style width from Thymeleaf.
     Capture the intended width, collapse to 0, then transition in. */
  function animateProgressBars() {
    var bars = document.querySelectorAll(".progress i");
    bars.forEach(function (bar, index) {
      var target = bar.style.width || "0%";
      bar.style.width = "0%";
      setTimeout(function () {
        bar.style.width = target;
      }, 120 + index * 60);
    });
  }

  /* Fade + slide table rows in with a small stagger */
  function staggerTableRows() {
    var rows = document.querySelectorAll("tbody tr");
    rows.forEach(function (row, index) {
      row.style.animationDelay = (index * 0.05) + "s";
    });
  }

  /* Subtle ripple feedback on primary buttons */
  function wireButtonRipples() {
    var buttons = document.querySelectorAll(".btn");
    buttons.forEach(function (btn) {
      btn.addEventListener("click", function (e) {
        var circle = document.createElement("span");
        var rect = btn.getBoundingClientRect();
        var size = Math.max(rect.width, rect.height);
        circle.style.position = "absolute";
        circle.style.width = circle.style.height = size + "px";
        circle.style.left = (e.clientX - rect.left - size / 2) + "px";
        circle.style.top = (e.clientY - rect.top - size / 2) + "px";
        circle.style.borderRadius = "50%";
        circle.style.background = "rgba(255,255,255,.35)";
        circle.style.pointerEvents = "none";
        circle.style.transform = "scale(0)";
        circle.style.transition = "transform .5s ease, opacity .5s ease";
        btn.appendChild(circle);
        requestAnimationFrame(function () {
          circle.style.transform = "scale(1)";
          circle.style.opacity = "0";
        });
        setTimeout(function () { circle.remove(); }, 500);
      });
    });
  }

  /* Auto-dismiss the success alert banner after a few seconds */
  function autoDismissAlert() {
    var alert = document.querySelector(".alert");
    if (!alert) return;
    setTimeout(function () {
      alert.style.transition = "opacity .5s ease, transform .5s ease, max-height .5s ease, margin .5s ease, padding .5s ease";
      alert.style.opacity = "0";
      alert.style.transform = "translateY(-8px)";
      alert.style.maxHeight = alert.offsetHeight + "px";
      requestAnimationFrame(function () {
        alert.style.maxHeight = "0";
        alert.style.margin = "0";
        alert.style.padding = "0 18px";
        alert.style.overflow = "hidden";
      });
    }, 4000);
  }
})();
