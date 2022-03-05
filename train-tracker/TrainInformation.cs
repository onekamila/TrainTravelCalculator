using System;

namespace train_tracker
{
    public class TrainInformation
    {
        public string LineName { get; set; }

        public string HistoricalTimeDelay { get; set; }

        public string CurrentStatus { get; set; }

        public string CurrentDepartureTime { get; set; }

        public string ScheduledDepartureTime { get; set; }
    }
}
