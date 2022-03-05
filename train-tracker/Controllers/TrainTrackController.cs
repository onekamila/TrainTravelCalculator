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

        [HttpGet]
        public IEnumerable<TrainInformation> Get()
        {
            TrainInformation trainInfo = new()
			{
                LineName = "North East Regional",
                HistoricalTimeDelay = "1 - 2 min",
                CurrentStatus = "On Time",
                CurrentDepartureTime = new DateTime(2022, 3, 3, 5, 40, 00).ToString("MM/dd hh:mm tt"),
                ScheduledDepartureTime = new DateTime(2022, 3, 3, 5, 40, 00).ToString("MM/dd hh:mm tt")
            };

            return new List<TrainInformation>() { trainInfo };
        }
    }
}
