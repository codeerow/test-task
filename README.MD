Tasks:
Listen to the BOOT_EVENT, create Broadcast Receiver
Create Database to persist information about BOOT_EVENT (id, date) + log option
Create WorkManager which will show a notification
Show “Special” body
Schedule logic (set WorkManager to update notification every 15 minutes)
Add dismiss action to the notification (notification should re-appear after 15 minutes)
Add customization options for notifications scheduling (count of dismissals and time fields (time
shouldn’t be less than 15 minutes))

15-10 minutes before deadline create TODOs

TODOs in code:

- TODO: migrate to ksp
- TODO: magic string (BootNotificationWorker)
- TODO: will not work for Android 14, make it non-cancellable somehow or find a way how to listen
  for cancellation
- TODO: ask for permissions on app start (for now for Android 14 you need to give the notification
  permission in the app settings by yourself)
- TODO: clean up (MainActivity)
- TODO: remove with viewbinding (MainActivity)

TODOs:

- Refactor Activity;
- Add UseCases and create separated component for dismissals count and time management;
- Inverse dependencies for boot info and dismissals logic; (i think it is optional for now, a little
  project structure refactoring will be enough)
- Add other stuff like logger, linters, analytics, unit/ui tests...;
- Add better support for Android 14;
- Investigate if it is possible to listen for notification cancel event;

- In general I just need to clean up the code, separate common logic into components, fix some
  issues.

Notes:
I'm not sure that i need to reset this value (marked as TODO in MainActivity)
I'm not sure that i understood the "Restore the notification with the updated information on the boot event ONLY IF the notification was present before the (re)boot.
" requirement 

Testing:
I was using Pixel 6 Pro API 34 (without gms) for testing
To emit android.intent.action.BOOT_COMPLETED i used next command:
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -p com.example.testtaskaura 
