using System;

namespace train_tracker
{
	public class TrainInformation {
		public string TrainName { get; set; }

		public string RouteName { get; set; }
		public string ScheduledDeparture { get; set; }
		public string ScheduledArrival { get; set; }
		public string HistoricalDepartureDelay { get; set; }
		public string HistoricalArrivalDelay { get; set; }
	}
}
