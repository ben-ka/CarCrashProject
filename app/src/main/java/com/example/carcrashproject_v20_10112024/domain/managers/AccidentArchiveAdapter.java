package com.example.carcrashproject_v20_10112024.domain.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carcrashproject_v20_10112024.Data.db.models.Accident;
import com.example.carcrashproject_v20_10112024.Data.db.models.AccidentDocument;
import com.example.carcrashproject_v20_10112024.Data.db.models.Alarm;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AccidentDocumentsTableHelper;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AlarmsTableHelper;
import com.example.carcrashproject_v20_10112024.R;

import java.util.ArrayList;
import java.util.List;

public class AccidentArchiveAdapter extends RecyclerView.Adapter<AccidentArchiveAdapter.ViewHolder> {
    private List<Accident> accidents = new ArrayList<>();
    private Context context;
    private List<Alarm> alarms = new ArrayList<>();
    private AlarmsTableHelper alarmsTableHelper;
    private AccidentDocumentsTableHelper accidentDocumentsTableHelper;

    public AccidentArchiveAdapter(Context context, List<Accident> accidents) {
        this.context = context;
        this.accidents = accidents;
        alarmsTableHelper = new AlarmsTableHelper(context);
        for (Accident accident: accidents)
        {
            alarms.add(alarmsTableHelper.getAlarmFromAccident(accident));
        }
        accidentDocumentsTableHelper = new AccidentDocumentsTableHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_accident_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        Accident accident = accidents.get(position);
        holder.tvLocation.setText(String.format("%s + %s",alarm.getLatitude(), alarm.getLongitude()));
        holder.tvTime.setText(alarm.getDateTime());
        holder.tvResponseType.setText(String.valueOf(alarm.getAlarmOptionId()));

        AccidentDocument document = accidentDocumentsTableHelper.getDocumentByAccidentId(accident.getId());
        if (document != null && document.getFileData() != null) {
            byte[] imageData = document.getFileData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            holder.imgOptional.setVisibility(View.VISIBLE); // Show the ImageView
            holder.imgOptional.setImageBitmap(bitmap); // Set the image

        }
        else {
            holder.imgOptional.setVisibility(View.GONE); // Hide the ImageView if no image exists
        }
    }

    @Override
    public int getItemCount() {
        return alarms != null ? alarms.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation, tvTime, tvResponseType;
        ImageView imgOptional;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tv_location_value);
            tvTime = itemView.findViewById(R.id.tv_time_value);
            tvResponseType = itemView.findViewById(R.id.tv_response_type_value);
            imgOptional = itemView.findViewById(R.id.img_optional);
        }
    }

}
