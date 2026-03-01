# Release Notes - Equipment Requests Dashboard

## Features
- **Dashboard on Home Screen**: The "Equipment Requests" card is now prominently displayed on the Home (Landing Page) for Owners.
- **Request Management**: Owners can Accept or Reject booking requests.
- **Status Tracking**: The dashboard shows counts for both "Pending" and "Ongoing" (Confirmed) bookings.

## Fixes
- **Rejection Error**: Fixed the issue where rejecting a request would fail. Added `REJECTED` status to the backend.

## Instructions
1.  **Restart Backend**: Ensure the Spring Boot server is restarted to apply backend changes.
2.  **Re-install App**: Re-install the Android app to ensure layout changes are applied.
3.  **Login as Owner**: You will see the new dashboard on the Home screen.
