package weatherwallet.heaven.zion.weatherwallet.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import weatherwallet.heaven.zion.weatherwallet.R;

/**
 * Created by Zion on 22/08/15.
 */
public class AlertUserMessage extends DialogFragment  {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.error_msg)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.error_ok_button, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
