package bitcare.com.br.bitcare;

import android.os.AsyncTask;

/**
 * Created by Felipe on 08/10/2016.
 */

public class LoginTask extends AsyncTask<String, Void, String> {


    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... params) {

        return null;


    }
}
