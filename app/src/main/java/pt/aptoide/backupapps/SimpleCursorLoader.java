/*******************************************************************************
 * Copyright (c) 2012 rmateus.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package pt.aptoide.backupapps;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation.  See the framework SDK
 * documentation for a class overview.
 *
 * This was based on the CursorLoader class
 *
 * @author cristian
 */
public abstract class SimpleCursorLoader extends AsyncTaskLoader<Cursor> {
  private Cursor mCursor;

  public SimpleCursorLoader(Context context) {
    super(context);
  }

  /* Runs on the UI thread */
  @Override public void deliverResult(Cursor cursor) {
    if (isReset()) {
      // An async query came in while the loader is stopped
      if (cursor != null) {
        cursor.close();
      }
      return;
    }
    Cursor oldCursor = mCursor;
    mCursor = cursor;

    if (isStarted()) {
      super.deliverResult(cursor);
    }

    if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
      oldCursor.close();
    }
  }

  /**
   * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
   * will be called on the UI thread. If a previous load has been completed and is still valid
   * the result may be passed to the callbacks immediately.
   * <p/>
   * Must be called from the UI thread
   */
  @Override protected void onStartLoading() {
    if (mCursor != null) {
      deliverResult(mCursor);
    }
    if (takeContentChanged() || mCursor == null) {
      forceLoad();
    }
  }

  /**
   * Must be called from the UI thread
   */
  @Override protected void onStopLoading() {
    // Attempt to cancel the current load task if possible.
    cancelLoad();
  }

  @Override protected void onReset() {
    super.onReset();

    // Ensure the loader is stopped
    onStopLoading();

    if (mCursor != null && !mCursor.isClosed()) {
      mCursor.close();
    }
    mCursor = null;
  }

  @Override public void onCanceled(Cursor cursor) {
    if (cursor != null && !cursor.isClosed()) {
      cursor.close();
    }
  }

  /* Runs on a worker thread */
  @Override public abstract Cursor loadInBackground();
}
