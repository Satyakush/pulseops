# Troubleshooting Guide

## API returns 401

Check that the request contains valid authentication credentials.

## API returns 403

Check the authenticated user's role against the endpoint authorization rule.

## API returns 400

Inspect the validation message and request id. Correct the request rather than retrying unchanged input.

## API returns 404

Confirm the resource identifier and verify that the resource exists.

## API returns 500

Capture the request id, timestamp, endpoint, and response body. Use those values to correlate server-side logs before retrying.
