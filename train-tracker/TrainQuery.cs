using System;

namespace train_tracker
{
	public class TrainQuery {
		public string OriginStationId { get; set; }
		public string DestinationStationId { get; set; }
		public string TrainId { get; set; }
		public string ScheduledDeparture { get; set; }
		public string ScheduledArrival { get; set; }
	}
}
