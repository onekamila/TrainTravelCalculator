using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace train_tracker.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class TrainTrackController : ControllerBase
    {
        private readonly ILogger<TrainTrackController> _logger;

        public TrainTrackController(ILogger<TrainTrackController> logger)
        {
            _logger = logger;
        }

        [HttpPost]
        public IEnumerable<TrainInformation> Post([FromBody]TrainQuery query)
        {
            TrainInformation trainInfo = new()
            {
                TrainName = "123",
                RouteName = "North East Regional",
                HistoricalDepartureDelay = "1 - 2 min",
                HistoricalArrivalDelay = "0 min",
                ScheduledDeparture = query.ScheduledDeparture,
                ScheduledArrival = query.ScheduledArrival
            };

            return new List<TrainInformation>() { trainInfo };
        }
    }
}
