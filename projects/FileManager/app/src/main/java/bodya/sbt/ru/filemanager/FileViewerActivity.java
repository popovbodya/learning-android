package bodya.sbt.ru.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

public class FileViewerActivity extends Activity {

    private ListAdapter listAdapter;
    private ListView listView;
    private TextView currentDirTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_viewer_activity_main);

        listAdapter = new ListAdapter();
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = listAdapter.getItem(position);
                Intent intent = new Intent(FileViewerActivity.this, FileViewerActivity.class);
                intent.setData(Uri.fromFile(file));
                startActivity(intent);
            }
        });

        File currentDirectory = getCurrentDirectory();

        listAdapter.setDirList(currentDirectory.listFiles());

        currentDirTextView = (TextView) findViewById(R.id.current_dir_text_view);
        currentDirTextView.setText(getResources().getString(R.string.current_dir, currentDirectory.getAbsolutePath()));

    }

    private File getCurrentDirectory() {
        File currentDirectory;

        Uri data = getIntent().getData();
        if (data == null) {
            currentDirectory = Environment.getRootDirectory();
        } else {
            currentDirectory = new File(data.getPath());
        }
        return currentDirectory;
    }
}
