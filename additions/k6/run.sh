#!/usr/bin/env sh
k6 run --iterations=50 --summary-trend-stats="med,p(95),p(99,9)" gkt_script.js